package ast.nodes;

import lexical.Token;
import semantic.SymbolTable;
import semantic.Type;

public class NodeVariableLocal extends NodeCompoundExpression implements Node{
    protected Token variableIdToken;
    protected NodeCompoundExpression assignmentExpression;
    protected Type variableType;
    public NodeVariableLocal(Token variableIdToken, NodeCompoundExpression assignmentExpression) {
        this.variableIdToken = variableIdToken;
        this.assignmentExpression = assignmentExpression;
    }
    public NodeVariableLocal(Token variableIdToken, Type type) {
        this.variableIdToken = variableIdToken;
        variableType = type;
    }
    @Override
    public void check() {
        //TODO check name colission
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "Local variable "+variableIdToken.getLexeme()+" = ";
        if (assignmentExpression != null) {
            toReturn += assignmentExpression.getStructure();
        } else {
            toReturn += variableType.toString();
        }
        return toReturn;
    }
}
