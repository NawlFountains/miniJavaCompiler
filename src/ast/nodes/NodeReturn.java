package ast.nodes;

public class NodeReturn extends NodeSentence implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
