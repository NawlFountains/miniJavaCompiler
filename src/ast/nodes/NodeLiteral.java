package ast.nodes;

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
}
