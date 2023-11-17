package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeBlock;
import ast.nodes.NodeCompoundExpression;
import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
import lexical.SemanticException;
import lexical.Token;
import semantic.Type;
import semantic.entities.AttributeST;
import semantic.entities.MethodST;
import semantic.entities.RoutineST;

public class NodeAccessMetVar extends NodeAccess implements Node {
    protected Token methodOrVarToken;
    protected boolean assignable = false;
    public NodeAccessMetVar(Token methodOrVarToken) {
        //Assume is 'this"
        super(methodOrVarToken);
        this.methodOrVarToken = methodOrVarToken;
    }

    @Override
    public void check() throws SemanticException {
        boolean found = false;
        RoutineST routineEnvironment = getRootBlock().getRoutineEnvironment();
        if (isAttribute) {
            assignable = true;
            //Search in parameters
            found = isAccessingParameter(methodOrVarToken.getLexeme());
            if (found) {
                returnType = routineEnvironment.getParameterType(methodOrVarToken.getLexeme());
            }

            //Search in local variables

            NodeBlock pivotBlock = getParentBlock();
            while (!pivotBlock.isRoot() && !found) {
                if (pivotBlock.existsVariableWithName(methodOrVarToken.getLexeme())) {
                    found = true;
                    returnType = pivotBlock.getVariableType(methodOrVarToken.getLexeme());
                }
                else {
                    pivotBlock = pivotBlock.getParentBlock();
                }
            }
            if (!found && pivotBlock.existsVariableWithName(methodOrVarToken.getLexeme())) {
                found = true;
                returnType = pivotBlock.getVariableType(methodOrVarToken.getLexeme());
            }
            if (!found) {
                //Search in attributes
                for (AttributeST a : routineEnvironment.getOwnerClass().getAttributes()) {
                    if (methodOrVarToken.getLexeme().equals(a.getAttributeName())) {
                        found = true;
                        returnType = a.getAttributeType();
                        break;
                    }
                }
            } else {
                found = true;
            }
            if (!found)
                throw new SemanticException(methodOrVarToken.getLexeme(),methodOrVarToken.getLineNumber(),"No existe ninguna variable local ni atributo con nombre "+methodOrVarToken.getLexeme());
        } else {
            checkArgumentsList();
            //Check if exist method with same name, arity an type
            for (MethodST m: routineEnvironment.getOwnerClass().getMethods()) {
                if (methodOrVarToken.getLexeme().equals(m.getName())) {
                    if (argumentList.size() == m.getParameterTypeList().size()) {
                        if (sameParameterTypes(m.getParameterTypeList(),argumentTypeList)) {
                            found = true;
                            returnType = m.getReturnType();
                            break;
                        } else {
                            throw new SemanticException(methodOrVarToken.getLexeme(),methodOrVarToken.getLineNumber(),"Distinto tipo de parametros para el metodo "+m.getName());
                        }
                    } else {
                        throw new SemanticException(methodOrVarToken.getLexeme(),methodOrVarToken.getLineNumber(),"Distinta cantidad de parametros para el metodo "+m.getName());
                    }
                }
            }
            if (!found)
                throw new SemanticException(methodOrVarToken.getLexeme(), methodOrVarToken.getLineNumber(),"No existe el metodo "+methodOrVarToken.getLexeme()+" en la clase "+ routineEnvironment.getOwnerClass().getClassName());
        }
        standaloneReturnType = returnType;
        if (chainedNode != null) {
            chainedNode.check();
            returnType = chainedNode.getReturnType();
        }
    }

    @Override
    public boolean isAssignable() {
        if (chainedNode != null)
            assignable = chainedNode.isAssignable();
        return assignable;
    }
    public String getStructure() {
        String toReturn = "AccessMetVar "+methodOrVarToken.getLexeme();
        if (argumentList.size() > 0) {
            toReturn += "(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure()+" ";
            }
            toReturn += ")";
        }
        if (chainedNode != null) {
            toReturn += "."+chainedNode.getStructure();
        }
        return toReturn;
    }
    public void generateCodeForAssignment() throws CodeGenerationException {
        //We asume only assignment will ask this
        String variableName = methodOrVarToken.getLexeme();
        if (isAccessingParameter(variableName)) {
            CodeGenerator.getInstance().addLine("STORE "+searchParameterOffset(variableName));
        } else if (isAccessingAttribute(variableName)) {
            CodeGenerator.getInstance().addLine("LOAD 3; Cargo this");
            CodeGenerator.getInstance().addLine("SWAP");
            CodeGenerator.getInstance().addLine("STOREREF "+searchAttributeOffset(variableName)+" ; Apilo offset de atributo "+variableName);
        } else {
            //If it's not an attribute nor a parameter then its a local variable
            CodeGenerator.getInstance().addLine("STORE "+searchLocalVariableOffset(variableName)+" ; Apilo offset de variable local "+variableName);
        }
    }

}
