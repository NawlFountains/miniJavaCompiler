package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
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
    @Override
    public void check() throws SemanticException {
        //TODO returnExpression type must be conformant with method return type
        NodeBlock rootBlock = getRootBlock();
        System.out.println("NodeReturn:check:start");
//        if (returnExpression != null) {
//                returnExpression.check();
//                System.out.println("NodeReturn:check:notNull");
//                if (!rootBlock.getRoutineEnvironment().getReturnType().toString().equals(returnExpression.getReturnType().toString()))
//                    throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"El tipo de return no corresponde con el declarado por el metodo, se esperaba "+rootBlock.getRootBlock().getRoutineEnvironment().getReturnType().toString()+" pero se obtuvo "+returnExpression.getReturnType().toString());
//        } else {
//            if (!rootBlock.getRoutineEnvironment().getReturnType().toString().equals("void"))
//                throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"El tipo de return no corresponde con el declarado por el metodo, se esperaba "+rootBlock.getRootBlock().getRoutineEnvironment().getReturnType().toString()+" pero se obtuvo void");
//        }
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
