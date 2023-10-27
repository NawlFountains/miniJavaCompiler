package ast.nodes;

import lexical.SemanticException;
import semantic.entities.RoutineST;

public class NodeWhile extends NodeSentence implements Node{
    protected NodeCompoundExpression conditionalExpression;
    protected NodeSentence whileSentence;
    public NodeWhile(NodeCompoundExpression conditionalExpression, NodeSentence whileSentence) {
        this.conditionalExpression = conditionalExpression;
        this.whileSentence = whileSentence;
    }
    public void setRoutineEnvironment(RoutineST routineEnvironment) {
        System.out.println("NodeIf:setRoutieEnviroment:"+routineEnvironment);
        this.routineEnvironment = routineEnvironment;
        conditionalExpression.setRoutineEnvironment(routineEnvironment);
        whileSentence.setRoutineEnvironment(routineEnvironment);
    }
    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        conditionalExpression.addParentBlock(nodeBlock);
        whileSentence.addParentBlock(nodeBlock);
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
