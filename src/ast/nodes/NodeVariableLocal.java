package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import semantic.PrimitiveType;
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
    }
    public NodeVariableLocal(Token variableIdToken, Type type) {
        this.variableIdToken = variableIdToken;
        variableType = type;
    }
    @Override
    public void check() throws SemanticException{
        //Check if the assignmentExpression is correct
        if (assignmentExpression != null) {
            assignmentExpression.check();
            if (variableType == null) {
                variableType = assignmentExpression.getReturnType();
            } else {
                //If doing optional must check type declared and assignment match
            }
        }
        System.out.println("NodeVariableLocal["+variableIdToken.getLexeme()+"]:check():parentBlock"+parentBlock);

        NodeBlock rootBlock = getRootBlock();

        RoutineST referenceEnviroment = rootBlock.getRoutineEnvironment();
        System.out.println("referenceEnviroment:"+referenceEnviroment);
        //Check name doesn't collide with a parameter for this method
        if (!referenceEnviroment.existParameter(variableIdToken.getLexeme())) {
            //Check name doesn't collide with an existing attribute
            System.out.println("NodeVariableLocal:check:toInitFor");
            for (AttributeST at : referenceEnviroment.getOwnerClass().getAttributes()) {
                System.out.println("NodeVariableLocal:for:"+at.getAttributeName()+"=="+variableIdToken.getLexeme()+" is "+variableIdToken.getLexeme().equals(at.getAttributeName()));
                if (variableIdToken.getLexeme().equals(at.getAttributeName())) {
                    throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe un atributo con el identificador "+variableIdToken.getLexeme());
                }
            }
            //Check name doesn't collide with an existing local variable
            System.out.println("NodeVariableLocal:check:toCheckForLocalVariables");
            NodeBlock pivotBlock = getParentBlock();
            boolean found = false;
            System.out.println("NodeVariableLocal:check:startIteration pivotBlock "+pivotBlock+" who is root ? "+pivotBlock.isRoot());
            while (!pivotBlock.isRoot() && !found) {
                if (pivotBlock.existsVariableWithName(variableIdToken.getLexeme())) {
                    found = true;
                } else {
                    pivotBlock = pivotBlock.getParentBlock();
                }
            }
            System.out.println("NodeVariableLocal:check:outIteration");
            if (!found && pivotBlock.existsVariableWithName(variableIdToken.getLexeme())) {
                found = true;
            }
            System.out.println("NodeVariableLocal:check:nearFinish:found "+found);
            if (found)
                throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe una variable local con el identificador "+variableIdToken.getLexeme());
            else
                this.getParentBlock().insertLocalVariable(this);
        } else {
            throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe un parametro con el identificador "+variableIdToken.getLexeme());
        }
        System.out.println("Finish checking");
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
