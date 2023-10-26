package ast.nodes;

import lexical.SemanticException;
import lexical.Token;

public abstract class  NodeSentence implements Node{
    @Override
    public void check() throws SemanticException {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        return "shouldn't be able to see this";
    }
}
