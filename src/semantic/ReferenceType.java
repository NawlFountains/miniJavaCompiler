package semantic;

public class ReferenceType extends Type {
    protected String typeName;
    public ReferenceType(String className) {
        typeName = className;
    }
    public String toString(){
        return typeName;
    }
}
