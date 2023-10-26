package ast.nodes;

public class NodeReturn extends NodeSentence implements Node{
    protected NodeCompoundExpression returnExpression;
    public NodeReturn(){

    }
    public NodeReturn(NodeCompoundExpression returnExpression) {
        this.returnExpression = returnExpression;
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "Return ";
        if (returnExpression != null) {
            toReturn += returnExpression.getStructure();
        }
        return toReturn;
    }
}
