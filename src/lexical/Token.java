package lexical;

public class Token {
    private String id;
    private String lexeme;
    private int lineNumber;
    public Token(String id, String lexeme, int lineNumber) {
        this.id = id;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }
    public String getId() { return id; }
    public String getLexeme() { return lexeme; }
    public int getLineNumber() { return lineNumber; }
    public String toString() { return "("+id+","+lexeme+","+lineNumber+")";}
}

