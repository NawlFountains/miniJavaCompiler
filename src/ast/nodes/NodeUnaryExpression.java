package ast.nodes;

public class NodeUnaryExpression extends NodeCompoundExpression implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
