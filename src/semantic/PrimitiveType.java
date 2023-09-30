package semantic;

public class PrimitiveType extends Type{
    protected String typeName;
    public PrimitiveType(String className) {
        typeName = className;
    }
    public String toString(){
        return typeName;
    }
}
