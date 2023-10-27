package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeCompoundExpression;
import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;
import semantic.entities.AttributeST;
import semantic.entities.ClassST;
import semantic.entities.MethodST;

public class NodeChained extends NodeAccess implements Node {
    protected NodeChained nodeChained;
    protected NodeChained previousNode;
    protected NodeAccess previousNodeAccess;
    public NodeChained(NodeChained previousNode,Token token) {
        super(token);
        this.previousNode = previousNode;
        System.out.println("Created chained node with "+token.getLexeme());
    }
    public NodeChained(NodeAccess previousNodeAccess,Token token) {
        super(token);
        this.previousNodeAccess = previousNodeAccess;
        System.out.println("Created chained node with "+token.getLexeme());
    }
    @Override
    public void check() throws SemanticException {
        //TODO check if receiver has what this node refers, maybe we need to pass it
        //If previous node was an access node it could be a This access (this.x()) , a var access (class.var) or a Constructor access (new Class())
        // It cant be method because you cant reference a variable in method
        if (previousNodeAccess != null) {
            if (previousNodeAccess instanceof NodeAccessThis) {
                ClassST thisClass = previousNodeAccess.getParentBlock().getRoutineEnvironment().getOwnerClass();
                checkExistanceOfMetVar(thisClass);
            } else if (previousNodeAccess instanceof NodeAccessConstructor) {
                ClassST constructorClass = SymbolTable.getInstance().getClassWithName(previousNodeAccess.getToken().getLexeme());
                checkExistanceOfMetVar(constructorClass);
            } else if (previousNodeAccess instanceof NodeAccessMetVar ) {
                //
                if (isAttribute) {
                    if (previousNodeAccess.isAttribute) {

                    }
                }
            }
        } else if (previousNode.isAttribute) {
            //TODO access both attribute and method
        } else {
            //TODO previous was method , check from return type
        }
    }

    private void checkExistanceOfMetVar(ClassST topClass) throws SemanticException {
        boolean found = false;
        System.out.println("NodeChained:checkExistanceOfMetVar("+topClass.getClassName()+") of "+operandToken.getLexeme());
        if (isAttribute) {
            for (AttributeST a : topClass.getAttributes()) {
                    if (operandToken.getLexeme().equals(a.getAttributeName())) {
                        found = true;
                        break;
                    }
            }
            if (!found)
                throw new SemanticException(operandToken.getLexeme(), operandToken.getLineNumber(), "No existe ningun atributo con identificador "+operandToken.getLexeme()+" en "+ topClass.getClassName());
        } else {
            //If it's not an attribute check if the method corresponds
            for (MethodST m: topClass.getMethods()) {
                if (operandToken.getLexeme().equals(m.getName())) {
                    if (argumentList.size() == m.getParameterTypeList().size()) {
                        //TODO check parameter types
                        found = true;
                        break;
                    } else {
                        throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"Distinta cantidad de parametros para el metodo "+m.getName());
                    }
                }
            }
            if (!found)
                throw new SemanticException(operandToken.getLexeme(), operandToken.getLineNumber(),"No existe el metodo "+operandToken.getLexeme()+" en la clase "+ topClass.getClassName());
        }
    }

    public void addChainedNode(NodeChained nodeChained) {
        this.nodeChained = nodeChained;
    }

    @Override
    public boolean isAssignable() {
        boolean assignable = isAttribute;
        if (chainedNode != null)
            assignable = chainedNode.isAssignable();
        System.out.println(getStructure()+" asked if assignable "+isAttribute);
        return assignable;
    }

    @Override
    public String getStructure() {
        String toReturn = operandToken.getLexeme();
        if (argumentList.size() > 0) {
            toReturn +="(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure()+" ";
            }
            toReturn +=")";
        }
        if (nodeChained != null) {
            toReturn += "."+nodeChained.getStructure();
        }
        return toReturn;
    }
}
