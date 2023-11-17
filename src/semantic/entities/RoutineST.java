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
    protected ParameterST actualParameter;
    protected String routineName;
    protected ClassST ownerClass;
    protected InterfaceST ownerInterface;
    protected NodeBlock blockAST;
    protected Type returnType;
    private static final int OFFSET_TILL_PARAMETERS = 4;

    protected RoutineST(String routineName) {
        parameters = new HashMap<>();
        parametersTypeList = new ArrayList<>();
        this.routineName = routineName;
    }
    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
    public Type getReturnType() {
        return returnType;
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
    public int getParameterPosition(String parameterName) {
        int position = 0;
        for (ParameterST p : parameters.values()) {
            if (p.getParameterName().equals(parameterName)) {
                break;
            } else {
                position++;
            }
        }
        return OFFSET_TILL_PARAMETERS+position;
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
    public Type getParameterType(String parameterName) {
        Type toReturn = null;
        toReturn = parameters.get(parameterName).parameterType;
        return toReturn;
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
        String toReturn = "Name : "+routineName+"\n";
        if (blockAST != null)
        toReturn += blockAST.getStructure();
        return toReturn;
    }
}
