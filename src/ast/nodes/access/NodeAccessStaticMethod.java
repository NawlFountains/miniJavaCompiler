package ast.nodes.access;

import ast.nodes.Node;
import lexical.SemanticException;
import lexical.Token;

public class NodeAccessStaticMethod extends NodeAccess implements Node {
    protected Token methodToken;
    public NodeAccessStaticMethod(Token callerToken, Token methodToken) {
        super(callerToken);
        this.methodToken = methodToken;
    }

    @Override
    public void check() throws SemanticException {
        //TODO check method exits and its static
        System.out.println("NodeAccessStaticMethod:check()");
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
