package semantic.entities;

import semantic.Type;

public class AttributeST implements EntityST {
    protected String attributeName;
    protected Type attributeType;
    protected Boolean isStatic;
    public AttributeST(String attributeName) {
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
    public void isCorrectlyDeclared() {

    }

    @Override
    public void consolidate() {

    }
}
