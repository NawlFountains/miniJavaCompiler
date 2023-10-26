package ast.nodes;

import lexical.SemanticException;

public class NodeWhile extends NodeSentence implements Node{
    protected NodeCompoundExpression conditionalExpression;
    protected NodeSentence whileSentence;
    public NodeWhile(NodeCompoundExpression conditionalExpression, NodeSentence whileSentence) {
        this.conditionalExpression = conditionalExpression;
        this.whileSentence = whileSentence;
    }
    @Override
    public void check() throws SemanticException {
        conditionalExpression.check();
        whileSentence.check();
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        return "While \n"+conditionalExpression.getStructure()+"\n "+whileSentence.getStructure()+"\n";
    }
}
