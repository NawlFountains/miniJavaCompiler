package lexical;

public class LexicalException extends Exception{
    String lexeme;
    String description;
    int lineNumber;
    int columnNumber;
    public LexicalException(String lexeme, int lineNumber, int columnNumber, String description) {
        super("[Error:"+lexeme+"|"+lineNumber+"]");
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
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
    public int getColumnNumber() {
        return columnNumber;
    }
}
