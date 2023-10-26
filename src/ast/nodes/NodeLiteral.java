package ast.nodes;

import lexical.Token;
import semantic.PrimitiveType;
import semantic.ReferenceType;
import semantic.Type;

import javax.swing.text.ElementIterator;

public class NodeLiteral extends NodeOperand implements Node{
    public NodeLiteral(Token literalToken) {
        super(literalToken);
        if (literalToken.getId().equals("idClase"))
            returnType = new ReferenceType(literalToken.getId());
        else
            returnType = new PrimitiveType(literalToken.getId());
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
