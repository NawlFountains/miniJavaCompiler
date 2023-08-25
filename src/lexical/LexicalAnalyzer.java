package lexical;

import filemanager.FileManager;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class LexicalAnalyzer {
    private List reservedWords = new LinkedList<String>();
    private FileManager fileManager;
    private String lexeme;
    private char currentChar;
    private int MAX_INT_LENGTH = 9;

    public LexicalAnalyzer(FileManager fileManager) throws FileNotFoundException {
        this.fileManager = fileManager;
        nextCharacter();
        loadReservedWords();
    }

    private void loadReservedWords() {
        reservedWords.add("if");
        reservedWords.add("else");
        //TODO add reserved words
    }

    public Token nextToken() throws LexicalException{
        lexeme = "";
        return initialState();
    }

    Token initialState() throws LexicalException {
        if (Character.isUpperCase(currentChar)) {
            updateLexeme();
            nextCharacter();
            return idClassState();
        } else if (Character.isLowerCase(currentChar)) {
            updateLexeme();
            nextCharacter();
            return idMetVarState();
        } else if (Character.isDigit(currentChar)) {
            updateLexeme();
            nextCharacter();
            return intLiteralState();
        } else if (isPunctuation(currentChar)) {
            updateLexeme();
            nextCharacter();
            return punctuationState();
        } else if (isSingleArithmeticOperator(currentChar)) {
            updateLexeme();
            if (currentChar == '/') {
                return possibleComment();
            } else {
                nextCharacter();
                return defaultOperatorStates();
            }
        } else if (isCompoundArithmeticOperator(currentChar)) {
            updateLexeme();
            nextCharacter();
            return opWithEqualSignStates();
        } else if (currentChar == '&') {
            updateLexeme();
            nextCharacter();
            return opAndState();
        } else if (currentChar == '|') {
            updateLexeme();
            nextCharacter();
            return opOrState();
        } else if (currentChar == '\'') {
            updateLexeme();
            nextCharacter();
            return charLiteralStarterState();
        } else if (Character.isWhitespace(currentChar)) {
            //TODO remember to jump whitespaces, enters, etc
            nextCharacter();
            return initialState();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber());
        }
    }
    private Token possibleComment() {
        if (currentChar == '/') {
            int currentLine = fileManager.getCurrentLineNumber();
            while (currentLine == fileManager.getCurrentLineNumber()) {
                nextCharacter();
                //TODO capture if endOfFile
            }
        } else if (currentChar == '*') {
            char prevChar = ' ';
            while (prevChar != '*' && currentChar != '/') {
                prevChar = currentChar;
                nextCharacter();
                //TODO capture if endOfFile
            }
        }
        return defaultOperatorStates();
    }

    private boolean isCompoundArithmeticOperator(char c) {
        boolean toReturn = false;
        if ( c == '>' || c == '<' || c == '!' || c == '+' || c == '-' || c == '=')
            toReturn = true;
        return toReturn;
    }

    private boolean isSingleArithmeticOperator(char c) {
        boolean toReturn = false;
        if ( c == '%' || c == '/' || c == '*'){
            toReturn = true;
        }
        return toReturn;
    }

    Token punctuationState() {
        return new Token(getPunctuationId(currentChar),lexeme, fileManager.getCurrentLineNumber());
    }

    Token endOfFileState () {
        return new Token("EOF", lexeme, fileManager.getCurrentLineNumber());
    }

    Token idClassState() {
        if (Character.isLetterOrDigit(currentChar)) {
            updateLexeme();
            nextCharacter();
            return idClassState();
        } else {
            return new Token("idClase",lexeme,fileManager.getCurrentLineNumber());
        }
    }

    Token idMetVarState() {
        if (Character.isLetterOrDigit(currentChar) && !isReservedWord(lexeme)) {
            updateLexeme();
            nextCharacter();
            return idMetVarState();
        } else if (isReservedWord(lexeme)) {
            //TODO evaluar si es una comun, booleano o null
            return new Token("resWord",lexeme, fileManager.getCurrentLineNumber());
        } else {
            return new Token("idMetVar",lexeme,fileManager.getCurrentLineNumber());
        }
    }

    Token intLiteralState() throws LexicalException {
        if (Character.isDigit(currentChar) && lexeme.length() < MAX_INT_LENGTH) {
            updateLexeme();
            nextCharacter();
            return intLiteralState();
        } else if (lexeme.length() >= MAX_INT_LENGTH) {
            //TODO throw exception about
            return null;
        } else {
            throw new LexicalException(lexeme,fileManager.getCurrentLineNumber());
        }
    }

    Token charLiteralStarterState() throws LexicalException {
        if (currentChar != '\\' || currentChar != '\'' ) {
            updateLexeme();
            nextCharacter();
            return charLiteralFinishingState();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber());
        }
    }
    Token charLiteralFinishingState() throws LexicalException {
        if (currentChar == '\'') {
            updateLexeme();
            nextCharacter();
            return new Token("charLiteral",lexeme, fileManager.getCurrentLineNumber());
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber());
        }
    }

    Token opWithEqualSignStates() {
        if (currentChar == '=') {
            updateLexeme();
            nextCharacter();
            return defaultOperatorStates();
        } else {
            return new Token (getOperatorId(lexeme),lexeme, fileManager.getCurrentLineNumber());
        }
    }
    Token defaultOperatorStates() {
        return new Token(getOperatorId(lexeme),lexeme, fileManager.getCurrentLineNumber());
    }

    Token opAndState() throws LexicalException {
        if (currentChar == '&') {
            updateLexeme();
            nextCharacter();
            return defaultOperatorStates();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber());
        }
    }

    Token opOrState() throws LexicalException {
        if (currentChar == '|') {
            updateLexeme();
            nextCharacter();
            return defaultOperatorStates();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber());
        }
    }

    Token stringStarterState() throws LexicalException {
        if (currentChar != '\n' && currentChar != '\'' && currentChar != '\\' && currentChar != '"') {
            updateLexeme();
            nextCharacter();
            return stringStarterState();
        } else if (currentChar == '\\') {
            updateLexeme();
            nextCharacter();
            return stringEscapeCharState();
        } else if (currentChar == '"'){
            updateLexeme();
            nextCharacter();
            return stringFinishingState();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber());
        }
    }

    Token stringEscapeCharState() throws LexicalException {
        if (currentChar == '"') {
            updateLexeme();
            nextCharacter();
            return stringStarterState();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber());
        }
    }

    Token stringFinishingState() {
        return new Token ("stringLiteral",lexeme, fileManager.getCurrentLineNumber());
    }

    private boolean isPunctuation(char c) {
        boolean toReturn = false;
        //TODO could store them in an array or list and ask if it contains it
        if ( c == '(' || c == ')' || c =='{' || c =='}' || c ==';' || c ==',' || c =='.')
            toReturn = true;
        return toReturn;
    }
    private String getPunctuationId(char c) {
        String id = null;
        switch (c) {
            case '(':
                id = "openPar";
                break;
            case ')':
                id = "closePar";
                break;
            case '{':
                id = "openCurl";
                break;
            case '}':
                id = "closeCurl";
                break;
            case ';':
                id = "semiColon";
                break;
            case ',':
                id = "comma";
                break;
            case '.':
                id = "period";
        }
        return id;
    }
    private String getOperatorId(String op) {
        String id = null;
        switch (op) {
            case "+":
                id = "opAdd";
                break;
            case "-":
                id = "opSub";
                break;
            case "*":
                id = "opMul";
                break;
            case "%":
                id = "opIntDiv";
                break;
            case "/":
                id = "opDiv";
                break;
            case "<":
                id = "opLess";
                break;
            case "<=":
                id = "opLessEq";
                break;
            case "==":
                id = "opEq";
                break;
            case ">":
                id = "opGreater";
                break;
            case ">=":
                id = "opGreaterEq";
                break;
            case "&&":
                id = "opAnd";
                break;
            case "||":
                id = "opOr";
                break;
            case "!":
                id = "opNot";
                break;
            case "!=":
                id = "opNotEq";
                break;
        }
        return id;
    }
    private boolean isReservedWord(String word) {
        boolean toReturn = false;
        if (reservedWords.contains(word)) {
            toReturn = true;
        }
        return toReturn;
    }
    private void updateLexeme() {
        lexeme += currentChar;
    }

    private void nextCharacter() {
        currentChar = fileManager.getNextCharacter();
    }
    private void finishReadingFile() throws FileNotFoundException {
        fileManager.closeCurrentFile();
    }
}
