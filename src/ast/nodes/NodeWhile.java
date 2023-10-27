package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import semantic.entities.RoutineST;

public class NodeWhile extends NodeSentence implements Node{
    protected NodeCompoundExpression conditionalExpression;
    protected NodeSentence whileSentence;
    protected Token declarationToken;
    public NodeWhile(NodeCompoundExpression conditionalExpression, NodeSentence whileSentence,Token declarationToken) {
        this.conditionalExpression = conditionalExpression;
        this.whileSentence = whileSentence;
        this.declarationToken = declarationToken;
    }
    public void setRoutineEnvironment(RoutineST routineEnvironment) {
        this.routineEnvironment = routineEnvironment;
        conditionalExpression.setRoutineEnvironment(routineEnvironment);
        if (whileSentence != null)
         whileSentence.setRoutineEnvironment(routineEnvironment);
    }
    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        conditionalExpression.addParentBlock(nodeBlock);
        if (whileSentence != null)
            whileSentence.addParentBlock(nodeBlock);
    }
    @Override
    public void check() throws SemanticException {
        conditionalExpression.check();
        if (whileSentence != null)
            whileSentence.check();
        if (!conditionalExpression.getReturnType().toString().equals("boolean"))
            throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"La expresion del bucle while debe ser booleana");
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "While "+conditionalExpression.getStructure();
        if (whileSentence != null)
                toReturn += " "+whileSentence.getStructure();
        return toReturn;
    }
}
