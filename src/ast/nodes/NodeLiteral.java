package ast.nodes;

import lexical.Token;

import javax.swing.text.ElementIterator;

public class NodeLiteral extends NodeOperand implements Node{
    public NodeLiteral(Token literalToken) {
        super(literalToken);
    }

    @Override
    public void check() {
        //TODO couldnt be wrong this
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
