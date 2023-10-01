package semantic;

public class ReferenceType extends Type {
    public ReferenceType(String className) {
        typeName = className;
    }
    public String toString(){
        return typeName;
    }
}
