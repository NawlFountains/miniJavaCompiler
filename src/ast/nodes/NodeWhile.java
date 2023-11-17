package ast.nodes;

import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
import lexical.SemanticException;
import lexical.Token;
import semantic.entities.RoutineST;

import java.util.concurrent.locks.Condition;

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
    @Override
    public void generateCode() throws CodeGenerationException {
        String whileStartLabel = CodeGenerator.generateLabelForString("while_start");
        String whileFinishLabel = CodeGenerator.generateLabelForString("while_out");

        CodeGenerator.getInstance().addLine(whileStartLabel+": NOP");
        conditionalExpression.generateCode();

        //We negate the condition, if its true then we should go into the while block
        CodeGenerator.getInstance().addLine("BF "+whileFinishLabel);
        whileSentence.generateCode();
        CodeGenerator.getInstance().addLine("JUMP "+whileStartLabel);
        CodeGenerator.getInstance().addLine(whileFinishLabel+": NOP");

    }
}
