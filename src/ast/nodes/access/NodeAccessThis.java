package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeCompoundExpression;
import ast.nodes.access.NodeAccess;
import lexical.SemanticException;
import lexical.Token;

public class NodeAccessThis extends NodeAccess implements Node {
    public NodeAccessThis(Token operandToken) {
        super(operandToken);
    }

    @Override
    public void check() throws SemanticException {
        //TODO this will suffice?
        chainedNode.check();
    }

    public String getStructure() {
        String toReturn = "this";
        if (argumentList.size() > 0) {
            toReturn+="(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure();
            }
            toReturn +=")";
        }
        if (chainedNode != null) {
            toReturn += "."+chainedNode.getStructure();
        }
        return toReturn;
    }
}
