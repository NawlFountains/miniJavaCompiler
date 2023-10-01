package semantic;

public abstract class Type {
    protected String typeName;
    public boolean equals(Type type) {
        return typeName.equals(type.toString());
    }
}
