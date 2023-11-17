package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeCompoundExpression;
import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
import lexical.SemanticException;
import lexical.Token;
import semantic.ReferenceType;
import semantic.SymbolTable;
import semantic.entities.AttributeST;
import semantic.entities.ClassST;
import semantic.entities.MethodST;

public class NodeChained extends NodeAccess implements Node {
    protected NodeChained nodeChained;
    protected NodeChained previousNode;
    protected NodeAccess previousNodeAccess;
    public NodeChained(NodeChained previousNode,Token token) {
        super(token);
        this.previousNode = previousNode;
    }
    public NodeChained(NodeAccess previousNodeAccess,Token token) {
        super(token);
        this.previousNodeAccess = previousNodeAccess;
    }
    @Override
    public void check() throws SemanticException {
        //If previous node was an access node it could be a This access (this.x()) , a var access (class.var) or a Constructor access (new Class())
        if (previousNodeAccess != null) {
            if (previousNodeAccess instanceof NodeAccessThis) {
                ClassST thisClass = previousNodeAccess.getParentBlock().getRoutineEnvironment().getOwnerClass();
                checkExistanceOfMetVar(thisClass);
            } else if (previousNodeAccess instanceof NodeAccessConstructor) {
                ClassST constructorClass = SymbolTable.getInstance().getClassWithName(previousNodeAccess.getToken().getLexeme());
                checkExistanceOfMetVar(constructorClass);
            } else if (previousNodeAccess instanceof NodeAccessMetVar ) {
                if (previousCallReturnAClass(previousNodeAccess)) {
                    ClassST previousClassType = SymbolTable.getInstance().getClassWithName(previousNodeAccess.getReturnType().toString());
                    checkExistanceOfMetVar(previousClassType);
                } else
                    throw new SemanticException(operandToken.getLexeme(), operandToken.getLineNumber(),"No se puede hacer un llamado o accesso a un tipo primitivo");
            }
        } else {
            if (previousNode.getReturnType() instanceof ReferenceType) {
                ClassST previousClassType = SymbolTable.getInstance().getClassWithName(previousNode.getReturnType().toString());
                checkExistanceOfMetVar(previousClassType);
            } else
                throw new SemanticException(operandToken.getLexeme(), operandToken.getLineNumber(),"No se puede hacer un llamado o accesso a un tipo primitivo");
        }

        if (nodeChained != null) {
            nodeChained.check();
            returnType = nodeChained.getReturnType();
        }
    }

    private void checkExistanceOfMetVar(ClassST topClass) throws SemanticException {
        boolean found = false;
        if (isAttribute) {
            for (AttributeST a : topClass.getAttributes()) {
                    if (operandToken.getLexeme().equals(a.getAttributeName())) {
                        found = true;
                        returnType = a.getAttributeType();
                        break;
                    }
            }
            if (!found)
                throw new SemanticException(operandToken.getLexeme(), operandToken.getLineNumber(), "No existe ningun atributo con identificador "+operandToken.getLexeme()+" en "+ topClass.getClassName());
        } else {
            checkArgumentsList();
            //If it's not an attribute check if the method corresponds
            for (MethodST m: topClass.getMethods()) {
                if (operandToken.getLexeme().equals(m.getName())) {
                    if (argumentList.size() == m.getParameterTypeList().size()) {
                        if (sameParameterTypes(m.getParameterTypeList(),argumentTypeList)) {
                            found = true;
                            returnType = m.getReturnType();
                            break;
                        } else
                            throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"Distinto tipos de parametros para el metodo "+m.getName());
                    } else {
                        throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"Distinta cantidad de parametros para el metodo "+m.getName());
                    }
                }
            }
            if (!found)
                throw new SemanticException(operandToken.getLexeme(), operandToken.getLineNumber(),"No existe el metodo "+operandToken.getLexeme()+" en la clase "+ topClass.getClassName());
        }
    }

    public void addChainedNode(NodeChained nodeChained) {
        this.nodeChained = nodeChained;
    }

    @Override
    public boolean isAssignable() {
        boolean assignable = isAttribute;
        if (nodeChained != null)
            assignable = nodeChained.isAssignable();
        return assignable;
    }

    @Override
    public String getStructure() {
        String toReturn = operandToken.getLexeme();
        if (argumentList.size() > 0) {
            toReturn +="(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure()+" ";
            }
            toReturn +=")";
        }
        if (nodeChained != null) {
            toReturn += "."+nodeChained.getStructure();
        }
        return toReturn;
    }
    @Override
    public void generateCode() throws CodeGenerationException {
        if (!isAttribute) {
            if (chainedNode == null) {
                MethodST currentMethod = searchMethod();
                if (!currentMethod.isStatic()) {
                    for (NodeCompoundExpression n: argumentList) {
                        n.generateCode();
                        CodeGenerator.getInstance().addLine("SWAP");
                    }
                    if (!currentMethod.getReturnType().toString().equals("void")) {
                        CodeGenerator.getInstance().addLine("RMEM 1 ; Reservamos una locacion de memoria para guardar el resultado de "+currentMethod.getName());
                        CodeGenerator.getInstance().addLine("SWAP");
                    }
                    CodeGenerator.getInstance().addLine("DUP ");
                    CodeGenerator.getInstance().addLine("LOADREF 0; Apilo offset de la VT en el CIR (siempre 0)");
                    CodeGenerator.getInstance().addLine("LOADREF "+ currentMethod.getOffsetInVT() +"; Apilo offset del metodo "+operandToken.getLexeme()+" en la VT");
                    CodeGenerator.getInstance().addLine("CALL ; Llama al metodo en el tope de la pila");
                } else {
                    for (NodeCompoundExpression n: argumentList) {
                        n.generateCode();
                    }
                    CodeGenerator.getInstance().addLine("PUSH "+CodeGenerator.generateLabelForMethod(searchMethod())+" ; Apliamos el metodo");
                    CodeGenerator.getInstance().addLine("CALL ; Llama al metodo en el tope de la pila");
                }
            } else {
                chainedNode.generateCode();
            }
        } else {
            if (chainedNode == null && isAttribute) {
                if (previousNodeAccess != null) {
                    int offset=-1;
                    if (previousNodeAccess instanceof NodeAccessThis) {
                        ClassST thisClass = previousNodeAccess.getParentBlock().getRoutineEnvironment().getOwnerClass();
                        offset = searchAttributeOffset(thisClass);
                    } else if (previousNodeAccess instanceof NodeAccessConstructor) {
                        ClassST constructorClass = SymbolTable.getInstance().getClassWithName(previousNodeAccess.getToken().getLexeme());
                        offset = searchAttributeOffset(constructorClass);
                    } else if (previousNodeAccess instanceof NodeAccessMetVar ) {
                        ClassST previousClassType = SymbolTable.getInstance().getClassWithName(previousNodeAccess.getStandaloneReturnType().toString());
                        offset = searchAttributeOffset(previousClassType);
                    } else {
                        ClassST previousClassType = SymbolTable.getInstance().getClassWithName(previousNode.getReturnType().toString());
                        offset = searchAttributeOffset(previousClassType);
                    }
                    CodeGenerator.getInstance().addLine("LOADREF "+offset+" ; Apilo offset de atributo "+operandToken.getLexeme());
                }
            } else {
                chainedNode.generateCode();
            }
        }
    }
    private boolean previousCallReturnAClass(NodeAccess previousNode) {
        return previousNode.getReturnType() instanceof ReferenceType;
    }
    @Override
    protected MethodST searchMethod() {
        MethodST methodToReturn = null;
        if (previousNode != null) {
            ClassST classToSearchIn = SymbolTable.getInstance().getClassWithName(previousNode.getReturnType().toString());
            for (MethodST m: classToSearchIn.getMethods()) {
                if (operandToken.getLexeme().equals(m.getName())) {
                    if (argumentList.size() == m.getParameterTypeList().size()) {
                        if (sameParameterTypes(m.getParameterTypeList(),argumentTypeList)) {
                            methodToReturn = m;
                            break;
                        }
                    }
                }
            }
        } else {
            ClassST classToSearchIn = SymbolTable.getInstance().getClassWithName(previousNodeAccess.getStandaloneReturnType().toString());
            for (MethodST m: classToSearchIn.getMethods()) {
                if (operandToken.getLexeme().equals(m.getName())) {
                    if (argumentList.size() == m.getParameterTypeList().size()) {
                        if (sameParameterTypes(m.getParameterTypeList(),argumentTypeList)) {
                            methodToReturn = m;
                            break;
                        }
                    }
                }
            }
        }
        return methodToReturn;
    }
    public void generateCodeForAssignment() throws CodeGenerationException {
        if (chainedNode == null && isAttribute) {
            if (previousNodeAccess != null) {
                int offset=-1;
                if (previousNodeAccess instanceof NodeAccessThis) {
                    ClassST thisClass = previousNodeAccess.getParentBlock().getRoutineEnvironment().getOwnerClass();
                    offset = searchAttributeOffset(thisClass);
                } else if (previousNodeAccess instanceof NodeAccessConstructor) {
                    CodeGenerator.getInstance().addLine("LOAD 3; Cargo this");
                    ClassST constructorClass = SymbolTable.getInstance().getClassWithName(previousNodeAccess.getToken().getLexeme());
                    offset = searchAttributeOffset(constructorClass);
                } else if (previousNodeAccess instanceof NodeAccessMetVar ) {
                    CodeGenerator.getInstance().addLine("LOAD 3; Cargo this");
                    ClassST previousClassType = SymbolTable.getInstance().getClassWithName(previousNodeAccess.getStandaloneReturnType().toString());
                    offset = searchAttributeOffset(previousClassType);
                } else {
                    CodeGenerator.getInstance().addLine("LOAD 3; Cargo this");
                    ClassST previousClassType = SymbolTable.getInstance().getClassWithName(previousNode.getReturnType().toString());
                    offset = searchAttributeOffset(previousClassType);
                }
                CodeGenerator.getInstance().addLine("SWAP");
                CodeGenerator.getInstance().addLine("STOREREF "+offset+" ; Apilo offset de atributo "+operandToken.getLexeme());
            }
        } else {
            chainedNode.generateCodeForAssignment();
        }
    }
    private int searchAttributeOffset(ClassST topClass) {
        int offset = 1;
        for (AttributeST a : topClass.getAttributes()) {
            if (operandToken.getLexeme().equals(a.getAttributeName())) {
                break;
            } else {
                offset++;
            }
        }
        return offset;
    }
}
