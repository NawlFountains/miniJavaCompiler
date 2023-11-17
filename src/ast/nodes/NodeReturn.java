package ast.nodes;

import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
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
        NodeBlock rootBlock = getRootBlock();
        if (rootBlock.getRoutineEnvironment() instanceof ConstructorST)
            throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"No se puede hacer return en un constructor");
        if (returnExpression != null) {
            returnExpression.check();
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
    @Override
    public void generateCode() throws CodeGenerationException {
        CodeGenerator.getInstance().addLine("FMEM 0");
        returnExpression.generateCode();
        CodeGenerator.getInstance().addLine("STORE 4 ; Ponemos el resultado del tope de la pila en el retorno");
        CodeGenerator.getInstance().addLine("STOREFP ; Actualizar el fp para qeu apunte al RA llamador");
        CodeGenerator.getInstance().addLine("RET 1");
    }
}
