package ast.nodes;

import lexical.SemanticException;

public class NodeBinaryExpression extends NodeCompoundExpression implements Node{
    protected NodeCompoundExpression leftSide;
    protected NodeOperand operand;
    protected NodeCompoundExpression rightSide;
    public NodeBinaryExpression(NodeCompoundExpression leftSide, NodeOperand operand, NodeCompoundExpression rightSide){
        this.leftSide = leftSide;
        this.operand = operand;
        this.rightSide = rightSide;
        leftSide.addParentBlock(parentBlock);
        operand.addParentBlock(parentBlock);
        rightSide.addParentBlock(parentBlock);
        System.out.println("NodeBinaryExpression:created: leftside: "+leftSide.getStructure()+" operand : "+operand.getStructure()+" rightside: "+rightSide.getStructure()+" parentBlock"+parentBlock);
        returnType = operand.getReturnType();
    }

    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        leftSide.addParentBlock(nodeBlock);
        operand.addParentBlock(nodeBlock);
        rightSide.addParentBlock(nodeBlock);
    }
    @Override
    public void check() throws SemanticException {
        //TODO check left and right expression compatible with operand
        leftSide.check();
        operand.check();
        rightSide.check();
        typeConformity(leftSide,operand,rightSide);
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
