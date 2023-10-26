package ast.nodes;

import lexical.SemanticException;
import lexical.Token;

public class NodeOperand extends NodeCompoundExpression implements Node{
    protected Token operandToken;
    public NodeOperand(Token operandToken) {
        this.operandToken = operandToken;
    }
    @Override
    public void check() throws SemanticException {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        System.out.println("NodeOperand:getStruture:Start");
        return "Operand : "+operandToken.getLexeme()+"\n";
    }
}
