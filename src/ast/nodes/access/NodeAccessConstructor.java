package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeCompoundExpression;
import lexical.SemanticException;
import lexical.Token;
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
        System.out.println("NodeAccessConstructor:check():"+operandToken.getLexeme());
        ClassST classReferenceByConstructor = SymbolTable.getInstance().getClassWithName(operandToken.getLexeme());
        if (classReferenceByConstructor == null)
            throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"No existe ninguna clase con el nombre "+operandToken.getLexeme());
        //Check if arguments arity and type corresponds with an existing class
        ConstructorST constructorOfClassNamed = classReferenceByConstructor.getConstructor();
        if (constructorOfClassNamed.getParameterTypeList().size() != argumentList.size())
            throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"No coincide la aridad entre el constructor definido y el usado "+operandToken.getLexeme());
        //TODO Check for every parameter if type corresponds, we need to be able to know expression return type
        if (!sameParameterTypes(constructorOfClassNamed.getParameterTypeList(),argumentTypeList)) {
            System.out.println(constructorOfClassNamed.getParameterTypeList().toString()+" vs "+ argumentTypeList.toString());
            throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"No coincide ningun constructor de "+constructorOfClassNamed.getName()+" para los parametro especificados.");
        }
        System.out.println("NodeAccessConstructor:check:finished continue with chained node");
        if (chainedNode != null)
            chainedNode.check();
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
