package ast.nodes;

import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
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

    @Override
    public void generateCode() throws CodeGenerationException {
        switch (operandToken.getId()){
            case "opLess":
                CodeGenerator.getInstance().addLine("LT");
                break;
            case "opLessEq":
                CodeGenerator.getInstance().addLine("LE");
                break;
            case "opEq":
                CodeGenerator.getInstance().addLine("EQ");
                break;
            case "opGreater":
                CodeGenerator.getInstance().addLine("GT");
                break;
            case "opGreaterEq":
                CodeGenerator.getInstance().addLine("GE");
                break;
            case "opAnd":
                CodeGenerator.getInstance().addLine("AND");
                break;
            case "opOr":
                CodeGenerator.getInstance().addLine("OR");
                break;
            case "opNot":
                CodeGenerator.getInstance().addLine("NOT");
                break;
            case "opNotEq":
                CodeGenerator.getInstance().addLine("NOT");
                CodeGenerator.getInstance().addLine("EQ");
                break;
            case "opAdd":
                CodeGenerator.getInstance().addLine("ADD");
                break;
            case "opMul":
                CodeGenerator.getInstance().addLine("MUL");
                break;
            case "opSub":
                CodeGenerator.getInstance().addLine("SUB");
                break;
            case "opDiv":
                CodeGenerator.getInstance().addLine("DIV");
                break;
            case "opIntDiv":
                CodeGenerator.getInstance().addLine("MOD");
                break;
        }
    }
}
