package semantic.entities;

import lexical.SemanticException;

public interface EntityST {
    void checkDeclarations() throws SemanticException;
    void consolidate();
}
