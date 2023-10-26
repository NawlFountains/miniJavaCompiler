package ast.nodes;

import lexical.Token;

public abstract class  NodeSentence implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        return "should be able to see this";
    }
}
