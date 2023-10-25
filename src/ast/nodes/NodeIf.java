package ast.nodes;

public class NodeIf extends NodeSentence implements Node{
    protected NodeBlock thenBlock;
    protected NodeCompoundExpression conditionalExpression;
    protected NodeElse nodeElse;

    public NodeIf(NodeCompoundExpression conditionalExpression, NodeBlock thenBlock) {
        this.thenBlock = thenBlock;
        this.conditionalExpression = conditionalExpression;
    }

    public void addElse(NodeElse nodeElse) {
        this.nodeElse = nodeElse;
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
