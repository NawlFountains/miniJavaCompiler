package ast.nodes.access;

import ast.nodes.*;
import lexical.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class NodeAccess extends NodeOperand implements Node {
    protected List<NodeCompoundExpression> argumentList;
    protected NodeChained chainedNode;
    public NodeAccess(Token callerToken) {
        super(callerToken);
        argumentList = new ArrayList<>();
    }

    public void addChainingNode(NodeChained nodeChained){
        this.chainedNode = nodeChained;
    }
    public void addArgument(NodeCompoundExpression nodeArgument) {
        System.out.println("NodeAccess:ArgumentAdded:"+nodeArgument.getStructure());
        argumentList.add(nodeArgument);
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure(){
        String toReturn = "Access : "+operandToken.getLexeme()+"(";
        for (NodeCompoundExpression n : argumentList) {
            toReturn += n.getStructure();
        }
        toReturn += ")";
        if (chainedNode != null)
            toReturn += "."+chainedNode.getStructure();
        return toReturn;
    }
}
