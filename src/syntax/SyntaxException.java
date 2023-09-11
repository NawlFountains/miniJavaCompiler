package syntax;

import lexical.Token;

public class SyntaxException extends Exception{
    private Token token;
    private String tokenName;

    public SyntaxException(Token token, String tokenName) {
        super("[Error:"+token.getLexeme()+"|"+token.getLineNumber()+"]");
        this.token = token;
        this.tokenName = tokenName;
    }

    public String getDescription() {
        //TODO se podria hacer un mapeo de tipo de error y string a devolver
        return "Error Sintactico en linea "+token.getLineNumber()+": se esperaba "+tokenName+ " se encontro \""+token.getLexeme()+"\"";
    }
}
