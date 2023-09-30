package semantic.entities;

import lexical.Token;
import semantic.Type;

public class AttributeST implements EntityST {
    protected String attributeName;
    protected Type attributeType;
    protected Boolean isStatic;
    protected Token declarationToken;
    public AttributeST(Token token, String attributeName) {
        this.declarationToken = token;
        this.attributeName = attributeName;
    }
    public void setAttributeType(Type attributeType) {
        this.attributeType = attributeType;
    }
    public Type getAttributeType() {
        return attributeType;
    }
    public void setStatic(Boolean isStatic) {
        this.isStatic = isStatic;
    }
    public Boolean isStatic() {
        return isStatic;
    }
    @Override
    public void checkDeclarations() {

    }


    @Override
    public void consolidate() {

    }
}
