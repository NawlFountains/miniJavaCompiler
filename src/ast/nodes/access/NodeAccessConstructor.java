package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeCompoundExpression;
import lexical.Token;

public class NodeAccessConstructor extends NodeAccess implements Node {
    public NodeAccessConstructor(Token operandToken) {
        super(operandToken);
    }

    @Override
    public void check() {
        //TODO check constructor exists with same parameters
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "new "+operandToken.getLexeme();
        if (argumentList.size() > 0) {
            toReturn += "(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure()+" ";
            }
            toReturn += ")";
        }
        return toReturn;
    }
}
