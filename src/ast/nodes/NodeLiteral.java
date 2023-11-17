package ast.nodes;

import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
import lexical.Token;
import semantic.PrimitiveType;
import semantic.ReferenceType;


public class NodeLiteral extends NodeOperand implements Node{
    public NodeLiteral(Token literalToken) {
        super(literalToken);
        String typeString = "";
        if (operandToken.getId().equals("idClase") || operandToken.getId().equals("stringLiteral")) {
            typeString = (operandToken.getId().equals("stringLiteral"))?"String": operandToken.getLexeme();
            returnType = new ReferenceType(typeString);
        }else {
            switch (operandToken.getId()){
                case("intLiteral"):
                    typeString = "int";
                    break;
                case("floatLiteral"):
                    typeString = "float";
                    break;
                case("charLiteral"):
                    typeString = "char";
                    break;
                case("rw_false"):
                case ("rw_true"):
                    typeString = "boolean";
                    break;
                default:
                    typeString = "null";
            }
            returnType = new PrimitiveType(typeString);
        }
    }

    @Override
    public void check() {
    }

    @Override
    public boolean isAssignable() {
        return false;
    }

    @Override
    public void generateCode() throws CodeGenerationException {
        if (returnType instanceof ReferenceType) {
            //String type
            String referenceLabel = CodeGenerator.generateLabelForString(returnType.toString());
            CodeGenerator.getInstance().addLine(".DATA");
            CodeGenerator.getInstance().addLine(referenceLabel+": DW "+operandToken.getLexeme()+",0 ");
            CodeGenerator.getInstance().addLine(".CODE");
            CodeGenerator.getInstance().addLine("PUSH "+referenceLabel);
        } else
            CodeGenerator.getInstance().addLine("PUSH "+operandToken.getLexeme()+" ; Apliamos el "+returnType.toString()+" "+operandToken.getLexeme());
    }
}
