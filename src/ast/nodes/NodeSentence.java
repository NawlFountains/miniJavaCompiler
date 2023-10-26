package ast.nodes;

import lexical.SemanticException;
import lexical.Token;

public abstract class  NodeSentence implements Node{
    protected NodeBlock parentBlock;
    @Override
    public void check() throws SemanticException {

    }
    public void addParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }
    public NodeBlock getParentBlock() {
        return parentBlock;
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        return "shouldn't be able to see this";
    }
}
