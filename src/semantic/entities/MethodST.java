package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.ReferenceType;
import semantic.SymbolTable;
import semantic.Type;

import java.util.HashMap;

public class MethodST extends RoutineST implements EntityST {
    protected Type returnType;
    protected Token declarationToken;
    protected boolean isStatic;
    public MethodST(Token declarationToken,String methodName) {
        super(methodName);
        this.declarationToken = declarationToken;
        this.routineName = methodName;
    }
    public Token getDeclarationToken() {
        return declarationToken;
    }
    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
    public Type getReturnType() {
        return returnType;
    }
    public void setStatic(Boolean isStatic) {
        this.isStatic = isStatic;
    }
    public boolean isStatic() {
        return isStatic;
    }
    public boolean existParameter(String parameterName) {
        return parameters.get(parameterName) != null;
    }

    public void checkDeclarations() throws SemanticException {
        if (returnType.getClass().toString().equals("class semantic.ReferenceType")) {
            checkReturnReferenceType((ReferenceType) returnType);
        }
        for (ParameterST p : parameters.values()) {
            p.checkDeclarations();
        }
    }
    private void checkReturnReferenceType(ReferenceType type) throws SemanticException {
        if (SymbolTable.getInstance().getClassWithName(type.toString()) == null && SymbolTable.getInstance().getInterfaceWithName(type.toString()) == null ) {
            throw new SemanticException(type.toString(),declarationToken.getLineNumber(),"El tipo de retorno "+type.toString()+" no se encuentra declarado");
        }
    }

    @Override
    public void consolidate() {

    }
    public String toString() {
        String toReturn = returnType+" "+routineName+'(';
        for (ParameterST p : parameters.values()) {
            toReturn += p.toString()+" ,";
        }
        toReturn = toReturn.substring(0,toReturn.length()-2);
        toReturn += ")";
        return toReturn;
    }
    public boolean equals(MethodST method) {
        return super.equals(method) && returnType.equals(method.getReturnType()) && (isStatic == method.isStatic);
    }
}
