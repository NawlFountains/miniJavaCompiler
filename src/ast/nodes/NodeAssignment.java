package ast.nodes;

public class NodeAssignment implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAsignable() {
        return true;
    }
}
