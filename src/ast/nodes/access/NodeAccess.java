package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeChained;
import ast.nodes.NodeOperand;
import lexical.Token;

public abstract class NodeAccess extends NodeOperand implements Node {
    protected NodeChained chainedNode;
    public NodeAccess(Token callerToken) {
        super(callerToken);
    }

    public void addChainingNode(NodeChained nodeChained){
        this.chainedNode = chainedNode;
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure(){
        String toReturn = "Access : "+operandToken.getLexeme();
        if (chainedNode != null)
            toReturn += " "+chainedNode.getStructure();
        return toReturn;
    }
}
