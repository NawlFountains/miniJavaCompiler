package lexical;

public class LexicalException extends Exception{
    public LexicalException(String lexeme, String lineNumber) {
        super("[Error:"+lexeme+"|"+lineNumber+"]");
    }
}
