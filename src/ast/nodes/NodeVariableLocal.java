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
    public void addParentBlock(NodeBlock parentNode) {
        super.addParentBlock(parentNode);
        if (assignmentExpression != null)
            assignmentExpression.addParentBlock(parentNode);
    }
    @Override
    public void check() throws SemanticException{
        //Check if the assignmentExpression is correct
        if (assignmentExpression != null) {
            assignmentExpression.check();
            if (variableType == null) {
                variableType = assignmentExpression.getReturnType();
                if (variableType.toString().equals("void") || variableType.toString().equals("null"))
                    throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Una variable no puede ser de tipo void ni de tipo null");
            } else {
                //If doing optional must check type declared and assignment match
            }
        }

        NodeBlock rootBlock = getRootBlock();

        RoutineST referenceEnviroment = rootBlock.getRoutineEnvironment();
        //Check name doesn't collide with a parameter for this method
        if (!referenceEnviroment.existParameter(variableIdToken.getLexeme())) {
            //Check name doesn't collide with an existing attribute
            for (AttributeST at : referenceEnviroment.getOwnerClass().getAttributes()) {
                if (variableIdToken.getLexeme().equals(at.getAttributeName())) {
                    throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe un atributo con el identificador "+variableIdToken.getLexeme());
                }
            }
            //Check name doesn't collide with an existing local variable
            NodeBlock pivotBlock = getParentBlock();
            boolean found = false;
            while (!pivotBlock.isRoot() && !found) {
                if (pivotBlock.existsVariableWithName(variableIdToken.getLexeme())) {
                    found = true;
                } else {
                    pivotBlock = pivotBlock.getParentBlock();
                }
            }
            if (!found && pivotBlock.existsVariableWithName(variableIdToken.getLexeme())) {
                found = true;
            }
            if (found)
                throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe una variable local con el identificador "+variableIdToken.getLexeme());
            else
                this.getParentBlock().insertLocalVariable(this);
        } else {
            throw new SemanticException(variableIdToken.getLexeme(),variableIdToken.getLineNumber(),"Colision de nombres, ya existe un parametro con el identificador "+variableIdToken.getLexeme());
        }
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
