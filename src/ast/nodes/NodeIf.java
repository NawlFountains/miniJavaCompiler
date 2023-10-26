package ast.nodes;

public class NodeIf extends NodeSentence implements Node{
    protected NodeSentence thenSentence;
    protected NodeCompoundExpression conditionalExpression;
    protected NodeElse nodeElse;

    public NodeIf(NodeCompoundExpression conditionalExpression, NodeSentence thenSentence) {
        this.thenSentence = thenSentence;
        this.conditionalExpression = conditionalExpression;
        System.out.println("NodeIf:created:"+conditionalExpression.toString()+"+"+thenSentence.toString());
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
    public String getStructure() {
        System.out.println("NodeIf:getStructure:Start");
        String toReturn = "If \n";
        toReturn += "Condition "+conditionalExpression.getStructure()+"\n";
        toReturn += "Then sentence"+thenSentence.getStructure()+"\n";
        if (nodeElse != null) {
            toReturn += "Else sentence"+nodeElse.getStructure()+"\n";
        }
        return toReturn;
    }
}
