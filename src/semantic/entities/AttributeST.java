package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;
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
    public String getAttributeName() {
        return attributeName;
    }
    public Token getDeclarationToken() {
        return declarationToken;
    }
    public void setAttributeType(Type attributeType) {
        this.attributeType = attributeType;
    }
    public boolean isTypeReference() {
        return attributeType.getClass().toString().equals("class semantic.ReferenceType");
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
    public void checkDeclarations() throws SemanticException {
        if (isTypeReference() && SymbolTable.getInstance().getClassWithName(attributeType.toString()) == null && SymbolTable.getInstance().getInterfaceWithName(attributeType.toString()) == null) {
            throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"No esta definido el tipo "+attributeType.toString()+" para el atributo "+declarationToken.getLexeme());
        } else if (attributeType.toString().equals("void")) {
            throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"No se puede declarar un atributo de tipo void");
        }
    }


    @Override
    public void consolidate() {

    }
    public String toString() {
        return attributeType+" "+attributeName;
    }
}
