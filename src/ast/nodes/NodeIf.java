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
        System.out.println("NodeIf:created:"+conditionalExpression.getStructure()+"+"+thenSentence.getStructure());
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
        System.out.println("NodeIf:check:conditionalExpression");
        conditionalExpression.check();
        System.out.println("NodeIf:check:thenSentence");
        thenSentence.check();
        System.out.println("conditionalExpression "+conditionalExpression+" return type "+conditionalExpression.getReturnType());
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
