package ast.nodes;

import lexical.SemanticException;
import lexical.Token;

public class NodeUnaryExpression extends NodeCompoundExpression implements Node{

    protected NodeOperand unaryOperand;
    protected NodeCompoundExpression operand;
    public NodeUnaryExpression(NodeCompoundExpression operand) {
        this.operand = operand;
    }
    public void addUnaryOperand(NodeOperand nodeOperand) {
        this.unaryOperand = nodeOperand;
    }
    public void addParentBlock(NodeBlock parentBlock) {
        super.addParentBlock(parentBlock);
        operand.addParentBlock(parentBlock);
        if (unaryOperand != null)
            unaryOperand.addParentBlock(parentBlock);
    }
    @Override
    public void check() throws SemanticException {
        operand.check();
        if (unaryOperand != null) {
            unaryOperand.check();
            typeConformity(unaryOperand,operand);
        }
        returnType = operand.getReturnType();
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "Unary expression \n";
        if (unaryOperand != null) {
            toReturn += unaryOperand.getStructure() +" ";
        }
        toReturn += operand.getStructure()+"\n";
        return toReturn;
    }

    @Override
    public void generateCode() {
        //TODO add generate code for unary expression
    }
}
