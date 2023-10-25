package ast.nodes;

public abstract class NodeCompoundExpression extends NodeSentence implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
