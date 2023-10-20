package ast.nodes;

public class NodeOperand implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAsignable() {
        return false;
    }
}
