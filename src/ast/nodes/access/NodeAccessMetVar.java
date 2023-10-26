package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeCompoundExpression;
import ast.nodes.NodeOperand;
import lexical.Token;

public class NodeAccessMetVar extends NodeAccess implements Node {
    protected Token methodOrVarToken;
    public NodeAccessMetVar(Token methodOrVarToken) {
        //TODO assume is 'this"
        super(new Token ("","",-1));
        this.methodOrVarToken = methodOrVarToken;
    }

    @Override
    public void check() {
        //TODO check method or variable exist
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "AccessMetVar "+methodOrVarToken.getLexeme();
        if (argumentList.size() > 0) {
            toReturn += "(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure()+" ";
            }
            toReturn += ")";
        }
        if (chainedNode != null) {
            toReturn += "."+chainedNode.getStructure();
        }
        return toReturn;
    }
}
