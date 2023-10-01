package semantic.entities;

import lexical.SemanticException;
import lexical.Token;

import java.util.HashMap;

public class ConstructorST extends RoutineST implements EntityST {

    public ConstructorST(String name) {
        super(name);
    }
    @Override
    public void checkDeclarations() {

    }

    @Override
    public void consolidate() {

    }
    public String toString() {
        String toReturn = routineName+'(';
        for (ParameterST p : parameters.values()) {
            toReturn += p.toString()+" ,";
        }
        toReturn += ")";
        return toReturn;
    }
}
