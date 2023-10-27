package ast.nodes;

import lexical.SemanticException;
import lexical.Token;

public class NodeUnaryExpression extends NodeCompoundExpression implements Node{

    protected NodeOperand unaryOperand;
    protected NodeCompoundExpression operand;
    public NodeUnaryExpression(NodeCompoundExpression operand) {
        this.operand = operand;
        System.out.println("NodeUnaryExpression:created:"+operand);
    }
    public void addUnaryOperand(NodeOperand nodeOperand) {
        this.unaryOperand = nodeOperand;
        System.out.println("NodeUnaryExpression:addUnaryOperand:"+nodeOperand.toString());
    }
    public void addParentBlock(NodeBlock parentBlock) {
        super.addParentBlock(parentBlock);
        operand.addParentBlock(parentBlock);
        if (unaryOperand != null)
            unaryOperand.addParentBlock(parentBlock);
    }
    @Override
    public void check() throws SemanticException {
        System.out.println("NodeUnaryExpression::check");
        operand.check();
        if (unaryOperand != null) {
            System.out.println("NodeUnaryExpression::check:unaryOperand");
            unaryOperand.check();
            typeConformity(unaryOperand,operand);
        }
        returnType = operand.getReturnType();
        System.out.println("NodeUnaryExpression:check:finish");
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
