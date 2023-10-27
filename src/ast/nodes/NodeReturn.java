package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import semantic.entities.ConstructorST;
import semantic.entities.MethodST;

public class NodeReturn extends NodeSentence implements Node{
    protected NodeCompoundExpression returnExpression;
    protected Token declarationToken;
    public NodeReturn(Token declarationToken){
        this.declarationToken = declarationToken;
    }
    public NodeReturn(NodeCompoundExpression returnExpression, Token declarationToken) {
        this.declarationToken = declarationToken;
        this.returnExpression = returnExpression;
    }
    public void addParentBlock(NodeBlock nodeBlock) {
        super.addParentBlock(nodeBlock);
        if (returnExpression != null)
            returnExpression.addParentBlock(nodeBlock);
    }
    @Override
    public void check() throws SemanticException {
        System.out.println("NodeReturn:check:start");
        NodeBlock rootBlock = getRootBlock();
        System.out.println("NodeReturn:check:instanceOfCosntructor"+ (rootBlock.getRoutineEnvironment() instanceof ConstructorST));
        if (rootBlock.getRoutineEnvironment() instanceof ConstructorST)
            throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"No se puede hacer return en un constructor");
        if (returnExpression != null) {
            System.out.println("NodeReturn:check:start"+returnExpression.getStructure());
            returnExpression.check();
                System.out.println("NodeReturn:check:notNull");
                System.out.println("Return type expected"+returnExpression.getReturnType());
                if (!rootBlock.getRoutineEnvironment().getReturnType().toString().equals(returnExpression.getReturnType().toString()))
                    throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"El tipo de return no corresponde con el declarado por el metodo, se esperaba "+rootBlock.getRoutineEnvironment().getReturnType().toString()+" pero se obtuvo "+returnExpression.getReturnType().toString());
        } else {
            if (!rootBlock.getRoutineEnvironment().getReturnType().toString().equals("void"))
                throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"El tipo de return no corresponde con el declarado por el metodo, se esperaba "+rootBlock.getRoutineEnvironment().getReturnType().toString()+" pero se obtuvo void");
        }
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "Return ";
        if (returnExpression != null) {
            toReturn += returnExpression.getStructure();
        }
        return toReturn;
    }
}
