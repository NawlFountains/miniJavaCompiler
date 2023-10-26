package ast.nodes;

import lexical.Token;

public class NodeAssignment extends NodeCompoundExpression implements Node{
    protected NodeCompoundExpression leftSide;
    protected NodeCompoundExpression rightSide;
    public NodeAssignment(NodeCompoundExpression leftSide, NodeCompoundExpression rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return true;
    }
    public String getStructure() {
        System.out.println("NodeAssignment:getStructure:Start");
        return "Assignment \n"+leftSide.getStructure()+"\n = \n"+rightSide.getStructure()+"\n";
    }
}
