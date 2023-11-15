package ast.nodes;

import lexical.SemanticException;

public interface Node {
    void check() throws SemanticException;
    boolean isAssignable();
    String getStructure();

    void generateCode();

}
