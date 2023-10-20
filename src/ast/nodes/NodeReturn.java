package ast.nodes;

public class NodeReturn implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAsignable() {
        return false;
    }
}
