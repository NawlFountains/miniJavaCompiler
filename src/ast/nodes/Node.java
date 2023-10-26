package ast.nodes;

public interface Node {
    void check();
    boolean isAssignable();
    String getStructure();
}
