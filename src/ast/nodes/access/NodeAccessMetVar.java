package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeOperand;
import lexical.Token;

public class NodeAccessMetVar extends NodeAccess implements Node {
    protected Token methodOrVarToken;
    public NodeAccessMetVar(Token methodOrVarToken) {
        //TODO assume is 'this"
        super(new Token ("rw_this","this",-1));
        this.methodOrVarToken = methodOrVarToken;
    }

    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "AccessMetVar "+methodOrVarToken.getLexeme()+"\n";
        return toReturn;
    }
}
