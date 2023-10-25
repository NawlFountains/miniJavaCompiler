package ast.nodes;

public class NodeLiteral extends NodeOperand implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
