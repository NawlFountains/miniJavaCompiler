package semantic.entities;

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
        if (!parameters.isEmpty()) {
            toReturn += toReturn.substring(0,toReturn.length()-2);
        }
        toReturn += ")";
        return toReturn;
    }
}
