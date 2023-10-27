package ast.nodes;

import lexical.SemanticException;
import lexical.Token;

public class NodeAssignment extends NodeCompoundExpression implements Node{
    protected NodeCompoundExpression leftSide;
    protected NodeCompoundExpression rightSide;
    protected Token declarationToken;
    public NodeAssignment(NodeCompoundExpression leftSide, NodeCompoundExpression rightSide, Token declarationToken) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.declarationToken = declarationToken;
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
        System.out.println("NodeAssigmnet:check():completed:seeAssignment");
        if (!leftSide.isAssignable()) {
            //TODO throw exception
            System.out.println("Leftside was "+leftSide.getStructure()+" type "+leftSide);
            throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"El lado izquierdo no es asignable");
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
