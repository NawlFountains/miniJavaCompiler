package ast.nodes;

import lexical.SemanticException;
import semantic.entities.RoutineST;

public abstract class  NodeSentence implements Node{
    protected NodeBlock parentBlock;
    protected RoutineST routineEnvironment;
    @Override
    public void check() throws SemanticException {

    }
    public void setRoutineEnvironment(RoutineST routineEnvironment) {
        this.routineEnvironment = routineEnvironment;
    }
    public RoutineST getRoutineEnvironment() {
        return routineEnvironment;
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
    protected NodeBlock getRootBlock() {
        NodeBlock rootBlock = this.getParentBlock();
        while (!rootBlock.isRoot()) {
            rootBlock = rootBlock.getParentBlock();
        }
        return rootBlock;
    }
}
