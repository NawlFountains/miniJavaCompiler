package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import filemanager.CodeGenerator;

public class NodeAssignment extends NodeCompoundExpression implements Node{
    protected NodeCompoundExpression leftSide;
    protected NodeCompoundExpression rightSide;
    protected Token declarationToken;
    public NodeAssignment(NodeCompoundExpression leftSide, NodeCompoundExpression rightSide, Token declarationToken) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.declarationToken = declarationToken;
    }

    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        leftSide.addParentBlock(nodeBlock);
        rightSide.addParentBlock(nodeBlock);
    }

    @Override
    public void check() throws SemanticException {
        leftSide.check();
        rightSide.check();
        returnType = leftSide.getReturnType();
        if (!leftSide.isAssignable()) {
            throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"El lado izquierdo no es asignable");
        } else {
            typeConformityForAssignment(leftSide.getReturnType(),rightSide.getReturnType(),declarationToken);
        }
    }

    @Override
    public boolean isAssignable() {
        return true;
    }
    public String getStructure() {
        return "Assignment \n"+leftSide.getStructure()+"\n = \n"+rightSide.getStructure()+"\n";
    }

//    @Override
    public void generateCode() {
//        leftSide.generateCode();
//        rightSide.generateCode();
//        CodeGenerator.getInstance().addLine();
    }
}
