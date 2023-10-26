package ast.nodes;

import lexical.SemanticException;
import lexical.Token;

public class NodeAssignment extends NodeCompoundExpression implements Node{
    protected NodeCompoundExpression leftSide;
    protected NodeCompoundExpression rightSide;
    public NodeAssignment(NodeCompoundExpression leftSide, NodeCompoundExpression rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        System.out.println("NodeAssignment:created:"+leftSide+"+"+rightSide+" parentBlock"+parentBlock);
    }

    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        leftSide.addParentBlock(nodeBlock);
        rightSide.addParentBlock(nodeBlock);
    }

    @Override
    public void check() throws SemanticException {
        System.out.println("NodeAssignment:check():leftside:"+leftSide+":rightside:"+rightSide);
        leftSide.check();
        rightSide.check();
        if (!leftSide.isAssignable()) {
            //TODO throw exception
        }
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
