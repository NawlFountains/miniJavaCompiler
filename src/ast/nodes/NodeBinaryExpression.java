package ast.nodes;

public class NodeBinaryExpression extends NodeCompoundExpression implements Node{
    protected NodeCompoundExpression leftSide;
    protected NodeOperand operand;
    protected NodeCompoundExpression rightSide;
    public NodeBinaryExpression(NodeCompoundExpression leftSide, NodeOperand operand, NodeCompoundExpression rightSide){
        this.leftSide = leftSide;
        this.operand = operand;
        this.rightSide = rightSide;
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
