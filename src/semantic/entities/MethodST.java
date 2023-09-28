package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.Type;

import java.util.HashMap;

public class MethodST implements EntityST {
    protected String methodName;
    protected Type returnType;
    protected ParameterST actualParameter;
    protected boolean isStatic;
    protected HashMap<String,ParameterST> parameters;
    public MethodST(String methodName) {
        parameters = new HashMap<>();
        this.methodName = methodName;
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
    public void insertParameter(Token token, ParameterST parameterST) throws SemanticException {
        if (!existParameter(token.getLexeme())) {
            parameters.put(token.getLexeme(),parameterST);
            this.actualParameter = parameterST;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un parametro en este metodo con el identificador "+token.getLexeme());
        }
    }
    public boolean existParameter(String parameterName) {
        return parameters.get(parameterName) != null;
    }

    @Override
    public void checkDeclarations() {

    }

    @Override
    public void isCorrectlyDeclared() {

    }

    @Override
    public void consolidate() {

    }
}
