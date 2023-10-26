package ast.nodes;

import lexical.Token;

public class NodeArgument implements Node{
    protected Token type;
    protected Token name;
    public NodeArgument(Token type, Token name) {
        this.type =type;
        this.name = name;
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }

    @Override
    public String getStructure() {
        return null;
    }
}
