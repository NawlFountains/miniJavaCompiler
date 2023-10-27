package ast.nodes;

import lexical.SemanticException;

public class NodeUnaryExpression extends NodeCompoundExpression implements Node{

    protected NodeOperand unaryOperand;
    protected NodeOperand operand;
    public NodeUnaryExpression(NodeOperand operand) {
        this.operand = operand;
        System.out.println("NodeUnaryExpression:created:"+operand);
    }
    public void addUnaryOperand(NodeOperand nodeOperand) {
        this.unaryOperand = nodeOperand;
        System.out.println("NodeUnaryExpression:addUnaryOperand:"+nodeOperand.toString());
    }
    @Override
    public void check() throws SemanticException {
        System.out.println("NodeUnaryExpression::check");
        operand.check();
        if (unaryOperand != null) {
            System.out.println("NodeUnaryExpression::check:unaryOperand");
            unaryOperand.check();
            //TODO check if operand is in conformity with unaryOperand
            typeConformity(unaryOperand,operand);
        }
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        System.out.println("NodeUnaryExpression:getStructure:Start");
        String toReturn = "Unary expression \n";
        if (unaryOperand != null) {
            toReturn += unaryOperand.getStructure() +" ";
        }
        toReturn += operand.getStructure()+"\n";
        return toReturn;
    }
}
