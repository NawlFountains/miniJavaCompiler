package ast.nodes;

import lexical.SemanticException;
import lexical.Token;
import semantic.PrimitiveType;
import semantic.Type;

public class NodeOperand extends NodeCompoundExpression implements Node{
    protected Token operandToken;
    public NodeOperand(Token operandToken) {
        this.operandToken = operandToken;
        String operandID = operandToken.getId();
        switch (operandID){
            case "opLess":
            case "opLessEq":
            case "opEq":
            case "opGreater":
            case "opGreaterEq":
            case "opAnd":
            case "opOr":
            case "opNot":
            case "opNotEq":
                returnType = new PrimitiveType("boolean");
                break;
            default:
                returnType = new PrimitiveType("int");
        }
    }
    public Type getReturnType() {
        return returnType;
    }
    @Override
    public void check() throws SemanticException {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure() {
        return "Operand : "+operandToken.getLexeme()+"\n";
    }
}
