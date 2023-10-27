package ast.nodes;

import lexical.SemanticException;
import semantic.entities.RoutineST;

public class NodeIf extends NodeSentence implements Node{
    protected NodeSentence thenSentence;
    protected NodeCompoundExpression conditionalExpression;
    protected NodeElse nodeElse;

    public NodeIf(NodeCompoundExpression conditionalExpression, NodeSentence thenSentence) {
        this.thenSentence = thenSentence;
        this.conditionalExpression = conditionalExpression;
        System.out.println("NodeIf:created:"+conditionalExpression.getStructure()+"+"+thenSentence.toString());
    }

    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        conditionalExpression.addParentBlock(nodeBlock);
        thenSentence.addParentBlock(nodeBlock);
    }
    public void setRoutineEnvironment(RoutineST routineEnvironment) {
        System.out.println("NodeIf:setRoutieEnviroment:"+routineEnvironment+" to "+conditionalExpression+" and "+thenSentence);
        this.routineEnvironment = routineEnvironment;
        conditionalExpression.setRoutineEnvironment(routineEnvironment);
        thenSentence.setRoutineEnvironment(routineEnvironment);
    }

    public void addElse(NodeElse nodeElse) {
        this.nodeElse = nodeElse;
    }
    @Override
    public void check() throws SemanticException {
        conditionalExpression.check();
        thenSentence.check();
        System.out.println("conditionalExpression "+conditionalExpression.getStructure()+" return type "+conditionalExpression.getReturnType().toString());
        if (!conditionalExpression.getReturnType().toString().equals("boolean"))
            throw new SemanticException("test",99,"Expresion dentro del if debe ser booleana");
        //TODO check conditionalExpression is boolean
        if (nodeElse != null)
            nodeElse.check();
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
