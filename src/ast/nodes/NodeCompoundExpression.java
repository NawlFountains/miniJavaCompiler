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
    public void typeConformity(NodeOperand unaryOperator, NodeOperand operand) throws SemanticException{
        System.out.println("TypeConformity:Unary:"+unaryOperator+" and "+operand);
        if (unaryOperator.getReturnType().equals("boolean") && operand.getReturnType().equals("boolean")) {
        } else if (unaryOperator.getReturnType().equals("int") && operand.getReturnType().equals("int")) {
        } else {
            throw new SemanticException(operand.operandToken.getLexeme(),operand.operandToken.getLineNumber(),"No se puede aplicar el operador "+unaryOperator.operandToken.getLexeme()+" al tipo "+operand.getReturnType().toString());
        }
    }
    public void typeConformity(NodeCompoundExpression leftOperand,NodeOperand binaryOperand,NodeCompoundExpression rightOperand) throws SemanticException {
        String operand = binaryOperand.operandToken.getId();
        //TODO if (+, - , / , %) both int and return int
        if (isArithmeticOperator(operand) && leftOperand.getReturnType().toString().equals("int") && rightOperand.getReturnType().toString().equals("int")) {
            System.out.println("Arithmetic operation");
        } else if (isArithmeticComparator(operand) && leftOperand.getReturnType().toString().equals("int") && rightOperand.getReturnType().toString().equals("int")){
            //TODO if (<=, < , > , >=) both int and return boolean
            System.out.println("Arithmetic comparation");
        } else if (isBooleanOperator(operand) && leftOperand.getReturnType().toString().equals("boolean") && rightOperand.getReturnType().toString().equals("boolean")) {
            //TODO if (&&, ||) both boolean and return boolean
            System.out.println("Boolean comparision");
        } else if (isSurfaceComparator(operand)) {
            //TODO if (== , !=) check inheritance
            System.out.println("Surface comparision");
        } else {
            throw new SemanticException(binaryOperand.operandToken.getLexeme(),binaryOperand.operandToken.getLineNumber(),"El operador "+binaryOperand.operandToken.getLexeme()+" no puede ser aplicado a "+leftOperand.getReturnType().toString()+" y "+rightOperand.getReturnType().toString());
        }
    }
    private boolean isArithmeticOperator(String operandID) {
        System.out.println("isArithmeticOperator "+operandID+" is "+operandID.equals("opAdd"));
        return operandID.equals("opAdd") || operandID.equals("opSub") || operandID.equals("opProd") || operandID.equals("opIntDiv") || operandID.equals("opDiv");
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
