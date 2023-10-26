package semantic.entities;

import ast.nodes.NodeBlock;
import lexical.SemanticException;
import lexical.Token;
import semantic.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class RoutineST {
    protected HashMap<String,ParameterST> parameters;
    protected List<Type> parametersTypeList;
    protected HashMap<String,Type> localVariables;
    protected ParameterST actualParameter;
    protected String routineName;
    protected ClassST ownerClass;
    protected InterfaceST ownerInterface;
    protected NodeBlock blockAST;

    protected RoutineST(String routineName) {
        parameters = new HashMap<>();
        parametersTypeList = new ArrayList<>();
        localVariables = new HashMap<>();
        this.routineName = routineName;
    }
    public void addLocalVariable(String varName, Type varType) {
        localVariables.put(varName,varType);
    }
    public boolean existVariableWithName(String varName) {
        return localVariables.get(varName) != null;
    }
    public Type getVariableType(String varName) {
        return localVariables.get(varName);
    }
    public void insertParameter(Token token, ParameterST parameterST) throws SemanticException {
        if (!existParameter(token.getLexeme())) {
            parameters.put(token.getLexeme(),parameterST);
            parametersTypeList.add(parametersTypeList.size(),parameterST.getParameterType());
            this.actualParameter = parameterST;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un parametro en este constructor con el identificador "+token.getLexeme());
        }
    }
    public void setOwnerClass(ClassST classST) {
        this.ownerClass = classST;
    }
    public ClassST getOwnerClass() {
        return ownerClass;
    }
    public void setOwnerInterface(InterfaceST interfaceST) {
        this.ownerInterface = interfaceST;
    }
    public InterfaceST getOwnerInterface() {
        return ownerInterface;
    }
    public List<Type> getParameterTypeList() {
        return parametersTypeList;
    }
    public boolean existParameter(String parameterName) {
        return parameters.get(parameterName) != null;
    }
    public String getName() {
        return routineName;
    }
    public boolean equals(RoutineST compare) {
        return sameList(parametersTypeList,compare.getParameterTypeList());
    }

    private boolean sameList(List<Type> listOne, List<Type> listTwo) {
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
    public void setAST(NodeBlock blockAST) {
        this.blockAST = blockAST;
    }
    public String getASTStructure() {
        System.out.println("RoutineST:getASTStructure:Start:"+routineName);
        String toReturn = "Name : "+routineName+"\n";
        System.out.println("RoutineST:getASTStructure:blockAST");
        if (blockAST != null)
        toReturn += blockAST.getStructure();
        System.out.println("RoutineST:getASTStructure:blockAST:finished");
        return toReturn;
    }
}
