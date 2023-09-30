package semantic.entities;

import lexical.Token;
import semantic.Type;

public class ParameterST  implements EntityST {
    protected Type parameterType;
    protected String parameterName;
    protected Token declarationToken;
    public ParameterST(Token declarationToken,String parameterName) {
        this.declarationToken = declarationToken;
        this.parameterName = parameterName;
    }
    public String getParameterName() {
        return parameterName;
    }
    public void setParameterType(Type parameterType) {
        this.parameterType = parameterType;
    }
    public Type getParameterType() {return this.parameterType;}
    public boolean isParameterTypeReference() {
        return parameterType.getClass().equals("semantic.PrimitiveType");
    }
    @Override
    public void checkDeclarations() {

    }

    @Override
    public void consolidate() {

    }
}
