package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeCompoundExpression;
import lexical.SemanticException;
import lexical.Token;
import semantic.ReferenceType;
import semantic.SymbolTable;
import semantic.entities.ClassST;
import semantic.entities.ConstructorST;
import semantic.entities.ParameterST;
import semantic.entities.RoutineST;

public class NodeAccessConstructor extends NodeAccess implements Node {
    public NodeAccessConstructor(Token constructorToken) {
        super(constructorToken);
    }

    @Override
    public void check() throws SemanticException {
        //Check if class exists
        checkArgumentsList();
        ClassST classReferenceByConstructor = SymbolTable.getInstance().getClassWithName(operandToken.getLexeme());
        if (classReferenceByConstructor == null)
            throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"No existe ninguna clase con el nombre "+operandToken.getLexeme());
        //Check if arguments arity and type corresponds with an existing class
        ConstructorST constructorOfClassNamed = classReferenceByConstructor.getConstructor();
        if (constructorOfClassNamed.getParameterTypeList().size() != argumentList.size())
            throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"No coincide la aridad entre el constructor definido y el usado "+operandToken.getLexeme());
        if (!sameParameterTypes(constructorOfClassNamed.getParameterTypeList(),argumentTypeList)) {
            throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"No coincide ningun constructor de "+constructorOfClassNamed.getName()+" para los parametro especificados.");
        }
        returnType = new ReferenceType(operandToken.getLexeme());
        if (chainedNode != null) {
            chainedNode.check();
            returnType = chainedNode.getReturnType();
        }
    }

    public String getStructure() {
        String toReturn = "new "+operandToken.getLexeme();
        if (argumentList.size() > 0) {
            toReturn += "(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure()+" ";
            }
            toReturn += ")";
        }
        return toReturn;
    }
}
