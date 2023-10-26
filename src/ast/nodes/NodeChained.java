package ast.nodes;

import ast.nodes.access.NodeAccess;
import lexical.Token;

public class NodeChained extends NodeAccess implements Node {
    protected NodeChained nodeChained;
    public NodeChained(Token token) {
        super(token);
        System.out.println("Created chained node with "+token.getLexeme());
    }
    @Override
    public void check() {
        //TODO check if receiver has what this node refers, maybe we need to pass it
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
        String toReturn = operandToken.getLexeme();
        if (argumentList.size() > 0) {
            toReturn +="(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure()+" ";
            }
            toReturn +=")";
        }
        if (nodeChained != null) {
            toReturn += "."+nodeChained.getStructure();
        }
        return toReturn;
    }
}
