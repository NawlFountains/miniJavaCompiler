package ast.nodes;

import lexical.SemanticException;

public class NodeElse extends NodeSentence implements Node{
    protected NodeSentence elseSentence;

    public NodeElse(NodeSentence elseSentence) {
        this.elseSentence = elseSentence;
    }

    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        if (elseSentence != null)
            elseSentence.addParentBlock(nodeBlock);
    }
    @Override
    public void check() throws SemanticException {
        if (elseSentence != null)
            elseSentence.check();
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure(){
        String toReturn = "Else ";
        if (elseSentence != null)
            toReturn += elseSentence.getStructure();
        return toReturn;
    }
}
