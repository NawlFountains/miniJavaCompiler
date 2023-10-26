package ast.nodes;

public class NodeBinaryExpression extends NodeCompoundExpression implements Node{
    protected NodeCompoundExpression leftSide;
    protected NodeOperand operand;
    protected NodeCompoundExpression rightSide;
    public NodeBinaryExpression(NodeCompoundExpression leftSide, NodeOperand operand, NodeCompoundExpression rightSide){
        this.leftSide = leftSide;
        this.operand = operand;
        this.rightSide = rightSide;
        System.out.println("NodeBinaryExpression:created:"+leftSide.toString()+"+"+operand.toString()+"+"+rightSide.toString());
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        System.out.println("NodeBinaryExpression:getStructure:Start");
        String toReturn = "Binary expression \n";
        toReturn += "Left side \n "+leftSide.getStructure();
        toReturn +=  "operand "+ operand.getStructure();
        toReturn += "Right side \n"+ rightSide.getStructure()+"\n";
        return toReturn;
    }
}
