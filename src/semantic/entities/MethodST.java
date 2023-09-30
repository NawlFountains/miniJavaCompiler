package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.ReferenceType;
import semantic.SymbolTable;
import semantic.Type;

public class MethodST extends RoutineST implements EntityST {
    protected String methodName;
    protected Type returnType;
    protected Token declarationToken;
    protected boolean isStatic;
    public MethodST(Token declarationToken,String methodName) {
        super();
        this.declarationToken = declarationToken;
        this.methodName = methodName;
    }
    public Token getDeclarationToken() {
        return declarationToken;
    }
    public String getMethodName() {
        return methodName;
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
        if (returnType.getClass().equals("semantic.ReferenceType")) {
            checkReturnReferenceType((ReferenceType) returnType);
        }
        for (ParameterST p : parameters.values()) {
            p.checkDeclarations();
        }
    }
    private void checkReturnReferenceType(ReferenceType type) throws SemanticException {
        if (SymbolTable.getInstance().getClassWithName(type.toString()) == null) {
            throw new SemanticException(type.toString(),declarationToken.getLineNumber(),"El tipo de retorno "+type.toString()+" no se encuentra declarado");
        }
    }

    @Override
    public void consolidate() {

    }
}
