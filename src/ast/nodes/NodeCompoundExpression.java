package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import semantic.ReferenceType;
import semantic.SymbolTable;
import semantic.Type;
import semantic.entities.ClassST;

public abstract class NodeCompoundExpression extends NodeSentence implements Node{
    protected Type returnType;
    @Override
    public void check() throws SemanticException {

    }
    public Type getReturnType() {
        return returnType;
    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public void typeConformity(NodeOperand unaryOperator, NodeCompoundExpression operand) throws SemanticException{
        if (unaryOperator.getReturnType().toString().equals("boolean") && operand.getReturnType().toString().equals("boolean")) {
        } else if (unaryOperator.getReturnType().toString().equals("int") && operand.getReturnType().toString().equals("int")) {
        } else {
            throw new SemanticException(unaryOperator.operandToken.getLexeme(),unaryOperator.operandToken.getLineNumber(),"No se puede aplicar el operador "+unaryOperator.operandToken.getLexeme()+" al tipo "+operand.getReturnType().toString());
        }
    }
    public void typeConformity(NodeCompoundExpression leftOperand,NodeOperand binaryOperand,NodeCompoundExpression rightOperand) throws SemanticException {
        String operand = binaryOperand.operandToken.getId();
        if (isArithmeticOperator(operand) && leftOperand.getReturnType().toString().equals("int") && rightOperand.getReturnType().toString().equals("int")) {
        } else if (isArithmeticComparator(operand) && leftOperand.getReturnType().toString().equals("int") && rightOperand.getReturnType().toString().equals("int")){
        } else if (isBooleanOperator(operand) && leftOperand.getReturnType().toString().equals("boolean") && rightOperand.getReturnType().toString().equals("boolean")) {
        } else if (isSurfaceComparator(operand) && (leftOperand.getReturnType().toString().equals(rightOperand.getReturnType().toString()) || leftOperand.getReturnType().toString().equals("null") || leftOperand.getReturnType().toString().equals("null"))) {
            //Only ask to be the same type to be comparable
        } else {
            throw new SemanticException(binaryOperand.operandToken.getLexeme(),binaryOperand.operandToken.getLineNumber(),"El operador "+binaryOperand.operandToken.getLexeme()+" no puede ser aplicado a "+leftOperand.getReturnType().toString()+" y "+rightOperand.getReturnType().toString());
        }
    }
    private boolean isArithmeticOperator(String operandID) {
        return operandID.equals("opAdd") || operandID.equals("opSub") || operandID.equals("opMul") || operandID.equals("opIntDiv") || operandID.equals("opDiv");
    }
    private boolean isArithmeticComparator(String operandID) {
        return operandID.equals("opLess") || operandID.equals("opLessEq") || operandID.equals("opGreater") || operandID.equals("opGreaterEq");
    }
    private boolean isSurfaceComparator(String operandID) {
        return operandID.equals("opNotEq") || operandID.equals("opEq");
    }
    private boolean isBooleanOperator(String operandID) {
        return operandID.equals("opAnd") || operandID.equals("opOr");
    }
    public void typeConformityForAssignment(Type declaredType, Type assignmentType,Token declarationToken) throws SemanticException {
        //If both arent the same type
        if (!declaredType.toString().equals(assignmentType.toString())) {
            //But are both classes
            if (declaredType instanceof ReferenceType && assignmentType instanceof ReferenceType) {
                //But the right side is not a type that inherits from the left side
                if (!isAncestor(declaredType.toString(),assignmentType.toString()))
                    throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"No se puede asignar por tipos incompatible "+assignmentType.toString()+" no hereda de "+declaredType.toString());
            } else {
                throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"No se puede asignar un tipo primtivo a una clase ni viceversa a"+declaredType.toString()+" no se le puede asignar "+assignmentType.toString());
            }
        }
    }
    private boolean isAncestor(String ancestorName, String descendantName) {
        boolean isAncestor = false;
        ClassST pivotClass = SymbolTable.getInstance().getClassWithName(descendantName);
        if (pivotClass.getParentClassesNames().contains(ancestorName))
            isAncestor = true;
        else {
            for (String s: pivotClass.getParentClassesNames())
                isAncestor = isAncestor(ancestorName,s);
        }
        return isAncestor;
    }


}
