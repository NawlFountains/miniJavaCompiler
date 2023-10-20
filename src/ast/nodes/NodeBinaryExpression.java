package ast.nodes;

public class NodeBinaryExpression implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAsignable() {
        return false;
    }
}
