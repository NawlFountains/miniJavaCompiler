package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.Type;

import java.util.HashMap;

public class MethodST extends RoutineST implements EntityST {
    protected String methodName;
    protected Type returnType;
    protected boolean isStatic;
    public MethodST(String methodName) {
        super();
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
