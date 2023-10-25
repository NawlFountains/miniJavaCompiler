package ast.nodes;

public class NodeElse extends NodeSentence implements Node{
    protected NodeIf nodeIf;
    protected NodeBlock elseBlock;

    public NodeElse(NodeIf nodeIf, NodeBlock elseBlock) {
        this.nodeIf = nodeIf;
        this.elseBlock = elseBlock;
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
