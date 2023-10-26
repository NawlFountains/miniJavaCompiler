package ast.nodes;

import lexical.Token;

public class NodeChained implements Node {
    protected NodeChained nodeChained;
    protected Token firstChainedToken;
    public NodeChained(Token token) {
        firstChainedToken = token;
    }
    @Override
    public void check() {

    }
    public void addChainedNode(NodeChained nodeChained) {
        this.nodeChained = nodeChained;
    }

    @Override
    public boolean isAssignable() {
        return false;
    }

    @Override
    public String getStructure() {
        String toReturn = firstChainedToken.getLexeme();
        if (nodeChained != null)
            toReturn += " "+nodeChained.getStructure();
        return toReturn;
    }
}
