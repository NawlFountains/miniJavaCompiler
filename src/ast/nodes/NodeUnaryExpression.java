package ast.nodes;

public class NodeUnaryExpression implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAsignable() {
        return false;
    }
}
