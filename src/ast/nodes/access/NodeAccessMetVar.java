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
        //TODO assume is 'this"
        super(new Token ("","",-1));
        this.methodOrVarToken = methodOrVarToken;
    }

    @Override
    public void check() throws SemanticException {
        //TODO check method or variable exist
        System.out.println("NodeAccesMetVar:check():parentBlock"+parentBlock);
        boolean found = false;
        RoutineST routineEnvironment = getRootBlock().getRoutineEnvironment();
        if (isAttribute) {
            System.out.println("NodeAccessMetVar:check:attribute");
            //Serach in parameters
            found = routineEnvironment.existParameter(methodOrVarToken.getLexeme());

            //Search in local variables
            NodeBlock pivotBlock = getParentBlock();
            while (!pivotBlock.isRoot() && !found) {
                if (pivotBlock.existsVariableWithName(methodOrVarToken.getLexeme()))
                    found = true;
                else
                    pivotBlock = pivotBlock.getParentBlock();
            }
            if (!found && pivotBlock.existsVariableWithName(methodOrVarToken.getLexeme()))
                found = true;
            if (!found) {
                //Search in attributes
                for (AttributeST a : routineEnvironment.getOwnerClass().getAttributes()) {
                    if (methodOrVarToken.getLexeme().equals(a.getAttributeName())) {
                        found = true;
                        break;
                    }
                }
            } else {
                found = true;
                assignable = true;
            }
            if (!found)
                throw new SemanticException(methodOrVarToken.getLexeme(),methodOrVarToken.getLineNumber(),"No existe ninguna variable local ni atributo con nombre "+methodOrVarToken.getLexeme());
        } else {
            //Check if exist method with same name, arity an type
            for (MethodST m: routineEnvironment.getOwnerClass().getMethods()) {
                if (methodOrVarToken.getLexeme().equals(m.getName())) {
                    if (argumentList.size() == m.getParameterTypeList().size()) {
                        //TODO check parameter types
                        if (m.getParameterTypeList().equals(argumentTypeList)) {
                            found = true;
                            break;
                        } else {
                            throw new SemanticException(methodOrVarToken.getLexeme(),methodOrVarToken.getLineNumber(),"Distinto tipo de parametros para el metodo "+m.getName());
                        }
                    } else {
                        throw new SemanticException(methodOrVarToken.getLexeme(),methodOrVarToken.getLineNumber(),"Distinta cantidad de parametros para el metodo "+m.getName());
                    }
                }
            }
            assignable = false;
            if (!found)
                throw new SemanticException(methodOrVarToken.getLexeme(), methodOrVarToken.getLineNumber(),"No existe el metodo "+methodOrVarToken.getLexeme()+" en la clase "+ routineEnvironment.getOwnerClass().getClassName());
        }
        if (chainedNode != null) {
            chainedNode.check();
        }
    }

    @Override
    public boolean isAssignable() {
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
