package ast.nodes;

public class NodeCompoundExpression implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAsignable() {
        return false;
    }
}
