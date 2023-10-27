package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import semantic.Type;

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
        System.out.println("TypeConformity:Unary:"+unaryOperator.getStructure()+" and "+operand.getReturnType());
        if (unaryOperator.getReturnType().toString().equals("boolean") && operand.getReturnType().toString().equals("boolean")) {
        } else if (unaryOperator.getReturnType().toString().equals("int") && operand.getReturnType().toString().equals("int")) {
        } else {
            throw new SemanticException(unaryOperator.operandToken.getLexeme(),unaryOperator.operandToken.getLineNumber(),"No se puede aplicar el operador "+unaryOperator.operandToken.getLexeme()+" al tipo "+operand.getReturnType().toString());
        }
        System.out.println("TypeConformity:Unary:finish");
    }
    public void typeConformity(NodeCompoundExpression leftOperand,NodeOperand binaryOperand,NodeCompoundExpression rightOperand) throws SemanticException {
        String operand = binaryOperand.operandToken.getId();
        if (isArithmeticOperator(operand) && leftOperand.getReturnType().toString().equals("int") && rightOperand.getReturnType().toString().equals("int")) {
        } else if (isArithmeticComparator(operand) && leftOperand.getReturnType().toString().equals("int") && rightOperand.getReturnType().toString().equals("int")){
        } else if (isBooleanOperator(operand) && leftOperand.getReturnType().toString().equals("boolean") && rightOperand.getReturnType().toString().equals("boolean")) {
        } else if (isSurfaceComparator(operand) && (leftOperand.getReturnType().toString().equals(rightOperand.getReturnType().toString()) || leftOperand.getReturnType().toString().equals("null") || leftOperand.getReturnType().toString().equals("null"))) {
            //Only ask to be the same type to be comparable
        } else {
            System.out.println("About to throw exception");
            throw new SemanticException(binaryOperand.operandToken.getLexeme(),binaryOperand.operandToken.getLineNumber(),"El operador "+binaryOperand.operandToken.getLexeme()+" no puede ser aplicado a "+leftOperand.getReturnType().toString()+" y "+rightOperand.getReturnType().toString());
        }
        System.out.println("typeConformity:Binary:finish");
    }
    private boolean isArithmeticOperator(String operandID) {
        System.out.println("isArithmeticOperator "+operandID+" is "+operandID.equals("opMul"));
        return operandID.equals("opAdd") || operandID.equals("opSub") || operandID.equals("opMul") || operandID.equals("opIntDiv") || operandID.equals("opDiv");
    }
    private boolean isArithmeticComparator(String operandID) {
        System.out.println("isArithmeticComparator "+operandID);
        return operandID.equals("opLess") || operandID.equals("opLessEq") || operandID.equals("opGreater") || operandID.equals("opGreaterEq");
    }
    private boolean isSurfaceComparator(String operandID) {
        System.out.println("isSurfaceComparator "+operandID);
        return operandID.equals("opNotEq") || operandID.equals("opEq");
    }
    private boolean isBooleanOperator(String operandID) {
        System.out.println("isBooleanOperator "+operandID);
        return operandID.equals("opAnd") || operandID.equals("opOr");
    }



}
