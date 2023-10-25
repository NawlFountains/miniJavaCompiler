package ast.nodes;

public class NodeAssignment extends NodeSentence implements Node{
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return true;
    }
}
