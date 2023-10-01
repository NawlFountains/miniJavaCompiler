package semantic.entities;

import lexical.SemanticException;
import lexical.Token;

import java.util.HashMap;

public class ConstructorST extends RoutineST implements EntityST {
    protected String constructorName;

    public ConstructorST(String name) {
        super();
        constructorName = name;
    }
    @Override
    public void checkDeclarations() {

    }

    @Override
    public void consolidate() {

    }
    public String toString() {
        String toReturn = constructorName+'(';
        for (ParameterST p : parameters.values()) {
            toReturn += p.toString()+" ,";
        }
        toReturn += ")";
        return toReturn;
    }
}
