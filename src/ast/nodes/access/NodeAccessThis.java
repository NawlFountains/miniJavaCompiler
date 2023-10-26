package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.access.NodeAccess;
import lexical.Token;

public class NodeAccessThis extends NodeAccess implements Node {
    public NodeAccessThis(Token operandToken) {
        super(operandToken);
    }

    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        return "";
    }
}
