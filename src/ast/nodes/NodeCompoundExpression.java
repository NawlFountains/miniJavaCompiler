package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import semantic.Type;

public abstract class NodeCompoundExpression extends NodeSentence implements Node{
    protected Token declarationToken;
    protected Type returnType;
    @Override
    public void check() throws SemanticException {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
