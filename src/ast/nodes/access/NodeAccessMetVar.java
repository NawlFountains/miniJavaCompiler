package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeBlock;
import ast.nodes.NodeCompoundExpression;
import ast.nodes.NodeOperand;
import lexical.SemanticException;
import lexical.Token;
import semantic.entities.AttributeST;
import semantic.entities.MethodST;
import semantic.entities.RoutineST;

public class NodeAccessMetVar extends NodeAccess implements Node {
    protected Token methodOrVarToken;
    protected boolean assignable = false;
    public NodeAccessMetVar(Token methodOrVarToken) {
        //Assume is 'this"
        super(methodOrVarToken);
        this.methodOrVarToken = methodOrVarToken;
    }

    @Override
    public void check() throws SemanticException {
        System.out.println("NodeAccesMetVar:check():"+methodOrVarToken.getLexeme()+" isAttribute:"+isAttribute);
        boolean found = false;
        System.out.println(this+" parent is "+getParentBlock());
        RoutineST routineEnvironment = getRootBlock().getRoutineEnvironment();
        if (isAttribute) {

            assignable = true;
            //Search in parameters

            System.out.println("NodeAccessMetVar:check:parameter");
            found = routineEnvironment.existParameter(methodOrVarToken.getLexeme());
            if (found) {
                returnType = routineEnvironment.getParameterType(methodOrVarToken.getLexeme());
            }

            //Search in local variables

            System.out.println("NodeAccessMetVar:check:localVar");
            NodeBlock pivotBlock = getParentBlock();
            while (!pivotBlock.isRoot() && !found) {
                if (pivotBlock.existsVariableWithName(methodOrVarToken.getLexeme())) {
                    found = true;
                    returnType = pivotBlock.getVariableType(methodOrVarToken.getLexeme());
                }
                else {
                    pivotBlock = pivotBlock.getParentBlock();
                }
            }
            if (!found && pivotBlock.existsVariableWithName(methodOrVarToken.getLexeme())) {
                found = true;
                returnType = pivotBlock.getVariableType(methodOrVarToken.getLexeme());
            }
            if (!found) {
                //Search in attributes
                System.out.println("NodeAccessMetVar:check:attribute");
                for (AttributeST a : routineEnvironment.getOwnerClass().getAttributes()) {
                    if (methodOrVarToken.getLexeme().equals(a.getAttributeName())) {
                        System.out.println("NodeAccessMetVar:check:Found");
                        found = true;
                        returnType = a.getAttributeType();
                        break;
                    }
                }
                System.out.println("NodeAccessMetVar:check:finish");
            } else {
                found = true;
            }
            if (!found)
                throw new SemanticException(methodOrVarToken.getLexeme(),methodOrVarToken.getLineNumber(),"No existe ninguna variable local ni atributo con nombre "+methodOrVarToken.getLexeme());
        } else {
            //Check if exist method with same name, arity an type
            for (MethodST m: routineEnvironment.getOwnerClass().getMethods()) {
                if (methodOrVarToken.getLexeme().equals(m.getName())) {
                    if (argumentList.size() == m.getParameterTypeList().size()) {
                        if (sameParameterTypes(m.getParameterTypeList(),argumentTypeList)) {
                            found = true;
                            returnType = m.getReturnType();
                            break;
                        } else {
                            throw new SemanticException(methodOrVarToken.getLexeme(),methodOrVarToken.getLineNumber(),"Distinto tipo de parametros para el metodo "+m.getName());
                        }
                    } else {
                        throw new SemanticException(methodOrVarToken.getLexeme(),methodOrVarToken.getLineNumber(),"Distinta cantidad de parametros para el metodo "+m.getName());
                    }
                }
            }
            if (!found)
                throw new SemanticException(methodOrVarToken.getLexeme(), methodOrVarToken.getLineNumber(),"No existe el metodo "+methodOrVarToken.getLexeme()+" en la clase "+ routineEnvironment.getOwnerClass().getClassName());
        }
        if (chainedNode != null) {
            System.out.println("NodeAccesMetVar:chainedNode:check");
            chainedNode.check();
            returnType = chainedNode.getReturnType();
        }
    }

    @Override
    public boolean isAssignable() {
        if (chainedNode != null)
            assignable = chainedNode.isAssignable();
        System.out.println(getStructure()+" was asked if assignable "+assignable+" by "+this);
        return assignable;
    }
    public String getStructure() {
        String toReturn = "AccessMetVar "+methodOrVarToken.getLexeme();
        if (argumentList.size() > 0) {
            toReturn += "(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure()+" ";
            }
            toReturn += ")";
        }
        if (chainedNode != null) {
            toReturn += "."+chainedNode.getStructure();
        }
        return toReturn;
    }
}
