package ast.nodes.access;

import ast.nodes.Node;
import lexical.Token;

public class NodeAccessConstructor extends NodeAccess implements Node {
    public NodeAccessConstructor(Token operandToken) {
        super(operandToken);
    }

    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
