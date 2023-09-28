package lexical;

public class SemanticException extends Exception{
    String lexeme;
    String description;
    int lineNumber;

    public SemanticException(String lexeme, int lineNumber, String description) {
        super("[Error:"+lexeme+"|"+lineNumber+"]");
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.description = description;
    }
    public String getInvalidLexeme() {
        return lexeme;
    }
    public String getDescription() {
        return description;
    }
    public int getLineNumber() {
        return lineNumber;
    }
}
