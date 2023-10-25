package ast.nodes;

public class NodeWhile extends NodeSentence implements Node{
    protected NodeCompoundExpression conditionalExpression;
    protected NodeBlock whileBlock;
    public NodeWhile(NodeCompoundExpression conditionalExpression, NodeBlock whileBlock) {
        this.conditionalExpression = conditionalExpression;
        this.whileBlock = whileBlock;
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
}
