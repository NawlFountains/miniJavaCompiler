package ast.nodes;

import lexical.SemanticException;
import lexical.Token;

public abstract class NodeCompoundExpression extends NodeSentence implements Node{
    protected Token declarationToken;
    @Override
    public void check() throws SemanticException {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
