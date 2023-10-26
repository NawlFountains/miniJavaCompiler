package ast.nodes.access;

import ast.nodes.Node;
import lexical.Token;

public class NodeAccessStaticMethod extends NodeAccess implements Node {
    protected Token methodToken;
    public NodeAccessStaticMethod(Token callerToken, Token methodToken) {
        super(callerToken);
        this.methodToken = methodToken;
    }

    @Override
    public void check() {
        //TODO check method exits and its static
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
