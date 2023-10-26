package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import semantic.PrimitiveType;
import semantic.SymbolTable;
import semantic.Type;
import semantic.entities.AttributeST;
import semantic.entities.RoutineST;

public class NodeVariableLocal extends NodeCompoundExpression implements Node{
    protected Token variableIdToken;
    protected NodeCompoundExpression assignmentExpression;
    protected Type variableType;
    public NodeVariableLocal(Token variableIdToken, NodeCompoundExpression assignmentExpression) {
        this.variableIdToken = variableIdToken;
        this.assignmentExpression = assignmentExpression;
        //TODO If we didn't specify type we need to inffer it
        variableType = new PrimitiveType("int"); //Placeholder
    }
    public NodeVariableLocal(Token variableIdToken, Type type) {
        this.variableIdToken = variableIdToken;
        variableType = type;
    }
    @Override
    public void check() throws SemanticException{
        //TODO check name colission
        System.out.println("NodeVariableLocal["+variableIdToken.getLexeme()+"]:check():parentBlock"+parentBlock);
        RoutineST referenceEnviroment = parentBlock.getRoutineEnvironment();
        System.out.println("referenceEnviroment:"+referenceEnviroment);
        //Check name doesn't collide with a parameter for this method
        if (!referenceEnviroment.existParameter(variableIdToken.getLexeme())) {
            //Check name doesn't collide with an existing attribute
            System.out.println("NodeVariableLocal["+variableIdToken.getLexeme()+"]:check():getOwnerClass()"+referenceEnviroment.getOwnerClass());
            for (AttributeST at : referenceEnviroment.getOwnerClass().getAttributes()) {
                System.out.println("NodeVariableLocal:for:"+at.getAttributeName()+"=="+variableIdToken.getLexeme()+" is "+variableIdToken.getLexeme().equals(at.getAttributeName()));
                if (variableIdToken.getLexeme().equals(at.getAttributeName())) {
                    throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe un atributo con el identificador "+variableIdToken.getLexeme());
                }
            }
            //Check name doesn't collide with an existing local variable
            if (!referenceEnviroment.existVariableWithName(variableIdToken.getLexeme())) {
                //If it doesn't then we add it
                referenceEnviroment.addLocalVariable(variableIdToken.getLexeme(),variableType);
            } else {
                throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe una variable local con el identificador "+variableIdToken.getLexeme());
            }
        } else {
            throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe un parametro con el identificador "+variableIdToken.getLexeme());
        }
        //TODO after checking will it be sound to set the return type, because we need to check the expressions
        variableType = assignmentExpression.returnType;
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        String toReturn = "Local variable "+variableIdToken.getLexeme()+" = ";
        if (assignmentExpression != null) {
            toReturn += assignmentExpression.getStructure();
        } else {
            toReturn += variableType.toString();
        }
        return toReturn;
    }
}
