package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import semantic.entities.RoutineST;

public class NodeIf extends NodeSentence implements Node{
    protected NodeSentence thenSentence;
    protected NodeCompoundExpression conditionalExpression;
    protected NodeElse nodeElse;
    protected Token declarationToken;

    public NodeIf(NodeCompoundExpression conditionalExpression, NodeSentence thenSentence, Token declarationToken) {
        this.thenSentence = thenSentence;
        this.conditionalExpression = conditionalExpression;
        this.declarationToken = declarationToken;
    }

    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        conditionalExpression.addParentBlock(nodeBlock);
        if (thenSentence != null)
            thenSentence.addParentBlock(nodeBlock);
        if (nodeElse != null)
            nodeElse.addParentBlock(nodeBlock);
    }
    public void setRoutineEnvironment(RoutineST routineEnvironment) {
        this.routineEnvironment = routineEnvironment;
        conditionalExpression.setRoutineEnvironment(routineEnvironment);
        if (thenSentence != null)
            thenSentence.setRoutineEnvironment(routineEnvironment);
    }

    public void addElse(NodeElse nodeElse) {
        this.nodeElse = nodeElse;
    }
    @Override
    public void check() throws SemanticException {
        conditionalExpression.check();
        if (thenSentence != null)
            thenSentence.check();
        if (!conditionalExpression.getReturnType().toString().equals("boolean"))
            throw new SemanticException(declarationToken.getLexeme(), declarationToken.getLineNumber(),"Expresion dentro del if debe ser booleana");
        if (nodeElse != null)
            nodeElse.check();
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "If \n";
        toReturn += "Condition "+conditionalExpression.getStructure()+"\n";
        if (thenSentence != null)
           toReturn += "Then sentence"+thenSentence.getStructure()+"\n";
        if (nodeElse != null) {
            toReturn += "Else sentence"+nodeElse.getStructure()+"\n";
        }
        return toReturn;
    }
    @Override
    public void generateCode() {
        //TODO add generate code for if block
    }
}
