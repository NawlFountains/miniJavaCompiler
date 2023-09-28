package semantic.entities;

import semantic.Type;

public class ParameterST  implements EntityST {
    protected Type parameterType;
    protected String parameterName;
    public ParameterST(String parameterName) {
        this.parameterName = parameterName;
    }
    public String getParameterName() {
        return parameterName;
    }
    public void setParameterType(Type parameterType) {
        this.parameterType = parameterType;
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
