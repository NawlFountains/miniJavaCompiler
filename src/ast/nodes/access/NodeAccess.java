package ast.nodes.access;

import ast.nodes.*;
import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;
import semantic.Type;
import semantic.entities.AttributeST;
import semantic.entities.MethodST;
import semantic.entities.RoutineST;

import java.util.ArrayList;
import java.util.List;

public abstract class NodeAccess extends NodeOperand implements Node {
    protected List<NodeCompoundExpression> argumentList;
    protected List<Type> argumentTypeList;
    protected NodeChained chainedNode;
    protected Type standaloneReturnType;
    protected boolean isAttribute = true;
    public NodeAccess(Token callerToken) {
        super(callerToken);
        argumentList = new ArrayList<>();
        argumentTypeList = new ArrayList<>();
    }
    public void isNotAttribute() {
        isAttribute = false;
    }

    public void addChainingNode(NodeChained nodeChained){
        this.chainedNode = nodeChained;
    }
    public Token getToken(){
        return operandToken;
    }
    public void addArgument(NodeCompoundExpression nodeArgument) {
        argumentList.add(nodeArgument);
    }
    @Override
    public void check() throws SemanticException {
        
    }

    @Override
    public boolean isAssignable() {
        boolean assignable = false;
        if (chainedNode != null)
            assignable = chainedNode.isAssignable();
        return assignable;
    }
    public String getStructure(){
        String toReturn = "Access : "+operandToken.getLexeme()+"(";
        for (NodeCompoundExpression n : argumentList) {
            toReturn += n.getStructure();
        }
        toReturn += ")";
        if (chainedNode != null)
            toReturn += "."+chainedNode.getStructure();
        return toReturn;
    }
    boolean sameParameterTypes(List<Type> listOne, List<Type> listTwo) {
        boolean same = true;
        int i = 0;
        if(listOne.size() == listTwo.size()) {
            while (same && i < listOne.size()) {
                if (!listOne.get(i).equals(listTwo.get(i))) {
                    same = false;
                }
                i++;
            }
        } else {
            same = false;
        }
        return same;
    }
    protected void checkArgumentsList() throws SemanticException {
        argumentTypeList = new ArrayList<>();
        for (NodeCompoundExpression n : argumentList) {
            n.addParentBlock(this.getParentBlock());
            n.check();
            argumentTypeList.add(n.getReturnType());
        }
    }
    protected int searchLocalVariableOffset(String variableName) {
        boolean found = false;
        int position = -1;
        NodeBlock pivotBlock = getParentBlock();
        while (!pivotBlock.isRoot() && !found) {
            if (pivotBlock.existsVariableWithName(variableName)) {
                found = true;
                position = pivotBlock.getLocalVariablePosition(variableName);
            }
            else {
                pivotBlock = pivotBlock.getParentBlock();
            }
        }
        if (!found && pivotBlock.existsVariableWithName(variableName)) {
            found = true;
            position = pivotBlock.getLocalVariablePosition(variableName);
        }
        return position;
    }

    protected int searchAttributeOffset(String variableName) {
        RoutineST routineEnvironment = getRootBlock().getRoutineEnvironment();
        int position = 0;
        for (AttributeST a : routineEnvironment.getOwnerClass().getAttributes()) {
            if (variableName.equals(a.getAttributeName())) {
                break;
            } else {
                position++;
            }
        }
        return position+1;
    }

    protected int searchParameterOffset(String variableName) {
        RoutineST routineEnvironment = getRootBlock().getRoutineEnvironment();
        return routineEnvironment.getParameterPosition(variableName);
    }

    protected boolean isAccessingAttribute(String variableName) {
        RoutineST routineEnvironment = getRootBlock().getRoutineEnvironment();
        boolean found = false;
        for (AttributeST a : routineEnvironment.getOwnerClass().getAttributes()) {
            if (variableName.equals(a.getAttributeName())) {
                System.out.println("Is true "+variableName+" and "+a.getAttributeName());
                found = true;
                returnType = a.getAttributeType();
                break;
            }
        }
        return found;
    }

    protected boolean isAccessingParameter(String parameterName) {
        RoutineST routineEnvironment = getRootBlock().getRoutineEnvironment();
        return routineEnvironment.existParameter(parameterName);
    }

    protected MethodST searchMethod() {
        MethodST methodToReturn = null;
        System.out.println("searchMethod:start");
        for (MethodST m: routineEnvironment.getOwnerClass().getMethods()) {
            System.out.println("searchMethod:"+m);
            if (operandToken.getLexeme().equals(m.getName())) {
                if (argumentList.size() == m.getParameterTypeList().size()) {
                    if (sameParameterTypes(m.getParameterTypeList(),argumentTypeList)) {
                        methodToReturn = m;
                        break;
                    }
                }
            }
        }
        return methodToReturn;
    }
    public boolean hasChainedNode(){
        return chainedNode != null;
    }
    public void generateCode() throws CodeGenerationException {
        System.out.println("generateCode:NodeAccessMetVar:"+operandToken.getLexeme());
        if (!isAttribute) {
            if (chainedNode == null) {
                for (NodeCompoundExpression n: argumentList)
                    n.generateCode();
                System.out.println("generateCode:NodeChained:"+operandToken.getLexeme()+":chainedNode(null):start");
                MethodST currentMethod = searchMethod();
                System.out.println("generateCode:NodeChained:"+operandToken.getLexeme()+":chainedNode(null):method:"+currentMethod);
                if (!currentMethod.isStatic()) {
                    System.out.println("generateCode:NodeChained:"+operandToken.getLexeme()+":chainedNode(null):dynamic");
                    if (!currentMethod.getReturnType().toString().equals("void")) {
                        CodeGenerator.getInstance().addLine("RMEM 1 ; Reservamos una locacion de memoria para guardar el resultado de "+currentMethod.getName());
                        CodeGenerator.getInstance().addLine("SWAP");
                    }
                    CodeGenerator.getInstance().addLine("DUP ");
                    CodeGenerator.getInstance().addLine("LOADREF 0; Apilo offset de la VT en el CIR (siempre 0)");
                    CodeGenerator.getInstance().addLine("LOADREF "+ currentMethod.getOffsetInVT() +"; Apilo offset del metodo "+operandToken.getLexeme()+" en la VT");
                    CodeGenerator.getInstance().addLine("CALL ; Llama al metodo en el tope de la pila");
                } else {
                    System.out.println("generateCode:NodeChained:"+operandToken.getLexeme()+":chainedNode(null):static");
                    CodeGenerator.getInstance().addLine("PUSH "+CodeGenerator.generateLabelForMethod(searchMethod())+" ; Apliamos el metodo");
                    CodeGenerator.getInstance().addLine("CALL ; Llama al metodo en el tope de la pila");
                }
            } else {
                chainedNode.generateCode();
            }
        } else {
//            Search if Var is attribute, parameter o local
            String variableName = operandToken.getLexeme();
            if (isAccessingParameter(variableName)) {
                System.out.println("Is accessing parameter");
                CodeGenerator.getInstance().addLine("LOAD "+searchParameterOffset(variableName)+"; Apilo el valor en memoria del offset de "+variableName);
            } else if (isAccessingAttribute(variableName)) {
                CodeGenerator.getInstance().addLine("LOAD 3; Cargo this");
                CodeGenerator.getInstance().addLine("LOADREF "+searchAttributeOffset(variableName)+" ; Apilo offset de atributo "+variableName);
            } else {
                //If it's not an attribute nor a parameter then its a local variable
                System.out.println("isLocalVariable:"+variableName);
                System.out.println("localVariableOffset:"+variableName+":"+searchLocalVariableOffset(variableName));
                CodeGenerator.getInstance().addLine("LOAD "+searchLocalVariableOffset(variableName)+" ; Apilo offset de variable local "+variableName);
            }
            if (chainedNode != null)
                chainedNode.generateCode();
        }
    }
    public Type getStandaloneReturnType() {
        return standaloneReturnType;
    }
}
