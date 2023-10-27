package ast.nodes;

import lexical.Token;
import semantic.PrimitiveType;
import semantic.ReferenceType;


public class NodeLiteral extends NodeOperand implements Node{
    public NodeLiteral(Token literalToken) {
        super(literalToken);
        String typeString = "";
        if (literalToken.getId().equals("idClase") || literalToken.getId().equals("stringLiteral")) {
            typeString = (literalToken.getId().equals("stringLiteral"))?"String": literalToken.getLexeme();
            returnType = new ReferenceType(typeString);
        }else {
            switch (literalToken.getId()){
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
                    System.out.println("ERROR NOT PRIMITIVE TYPE");
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
