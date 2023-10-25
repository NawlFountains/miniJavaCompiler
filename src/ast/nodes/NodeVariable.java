package ast.nodes;

import semantic.Type;

public class NodeVariable extends NodeCompoundExpression implements Node{
    protected String variableName;
    protected Type variableType;
    public NodeVariable(String variableName, Type type) {
        this.variableName = variableName;
        this.variableType = type;
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
