package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;
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
        return parameterType.getClass().toString().equals("class semantic.ReferenceType");
    }
    @Override
    public void checkDeclarations() throws SemanticException {
        System.out.println("Is "+parameterName+" reference type parameter "+isParameterTypeReference());
        if (isParameterTypeReference() && SymbolTable.getInstance().getClassWithName(parameterType.toString()) == null && SymbolTable.getInstance().getInterfaceWithName(parameterType.toString()) == null) {
            throw new SemanticException(parameterType.toString(),declarationToken.getLineNumber(),"No esta declarado el tipo "+parameterType.toString()+" que usa el parametro "+declarationToken.getLexeme());
        }
    }

    @Override
    public void consolidate() {

    }
    public String toString() {
        String toReturn = parameterType+" "+parameterName;
        return toReturn;
    }
    public boolean equals(ParameterST parameterToCompare) {
        return parameterType.equals(parameterToCompare.getParameterType());
    }
}
