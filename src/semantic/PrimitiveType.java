package semantic;

public class PrimitiveType extends Type{
    public PrimitiveType(String className) {
        typeName = className;
    }
    public String toString(){
        return typeName;
    }
}
