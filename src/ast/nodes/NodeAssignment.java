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
        System.out.println("NodeAssignment:created:"+leftSide.getStructure()+"+"+rightSide.getStructure()+" parentBlock"+parentBlock);
    }

    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        leftSide.addParentBlock(nodeBlock);
        rightSide.addParentBlock(nodeBlock);
    }

    @Override
    public void check() throws SemanticException {
        System.out.println("NodeAssignment:check():leftside:"+leftSide.getStructure());
        leftSide.check();
        System.out.println("NodeAssignment:check():rightside:"+rightSide.getStructure());
        rightSide.check();
        System.out.println("NodeAssignment:check():completed:seeAssignment");
        returnType = leftSide.getReturnType();
        if (!leftSide.isAssignable()) {
            throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"El lado izquierdo no es asignable");
        } else {
            System.out.println("NodeAssignment:LeftType:"+leftSide.getReturnType());
            System.out.print("RightSide:"+rightSide.getReturnType());
            System.out.println();
            typeConformityForAssignment(leftSide.getReturnType(),rightSide.getReturnType(),declarationToken);
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
