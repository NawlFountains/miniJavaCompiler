package lexical;

import filemanager.FileManager;

import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class LexicalAnalyzer {
    private Dictionary<String,String> reservedWords = new Hashtable<>();
    private FileManager fileManager;
    private String lexeme;
    private char currentChar;
    private final int MAX_INT_LENGTH = 9;
    private final float MIN_FLOAT_VALUE = 1.40e-45f;
    private final float MAX_FLOAT_VALUE = 3.4028235e38f;

    //TODO fix always giving one column more from error either here or in FileManager
    public LexicalAnalyzer(FileManager fileManager) throws FileNotFoundException {
        this.fileManager = fileManager;
        nextCharacter();
        loadReservedWords();
    }

    private void loadReservedWords() {
        reservedWords.put("class","rw_class");
        reservedWords.put("public","rw_public");
        reservedWords.put("void","rw_void");
        reservedWords.put("if","rw_if");
        reservedWords.put("this","rw_this");
        reservedWords.put("interface","rw_interface");
        reservedWords.put("static","rw_static");
        reservedWords.put("boolean","rw_boolean");
        reservedWords.put("else","rw_else");
        reservedWords.put("new","rw_new");
        reservedWords.put("extends","rw_extends");
        reservedWords.put("implements","rw_implements");
        reservedWords.put("char","rw_char");
        reservedWords.put("while","rw_while");
        reservedWords.put("null","rw_null");
        reservedWords.put("int","rw_int");
        reservedWords.put("return","rw_return");
        reservedWords.put("true","rw_true");
        reservedWords.put("false","rw_false");
        reservedWords.put("var","rw_var");
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
        } else if (currentChar == '"') {
            updateLexeme();
            nextCharacter();
            return stringStarterState();
        } else if (currentChar == '\'') {
            updateLexeme();
            nextCharacter();
            return charLiteralStarterState();
        } else if (currentChar == '.') {
            updateLexeme();
            nextCharacter();
            return possibleFloatState();
        } else if (isPunctuation(currentChar)) {
            updateLexeme();
            nextCharacter();
            return punctuationState();
        } else if (currentChar == '/') {
            updateLexeme();
            nextCharacter();
            return possibleComment();
        } else if (isSingleArithmeticOperator(currentChar)) {
            updateLexeme();
            nextCharacter();
            return defaultOperatorStates();
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
        } else if (Character.isWhitespace(currentChar) || currentChar == '\n') {
            nextCharacter();
            return initialState();
        } else if (currentChar == (char) -1) {
            return endOfFileState();
        } else {
            updateLexeme();
            nextCharacter();
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber()-1,"no es un simbolo valido");
        }
    }

    private Token possibleFloatState() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            nextCharacter();
            return decFloatAfterPeriod();
        } else {
            return new Token(getPunctuationId(),lexeme,fileManager.getCurrentLineNumber());
        }
    }

    private Token decFloatAfterExponent() throws LexicalException{
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            nextCharacter();
            return decFloatAfterExponent();
        } else {
            if (isInFloatRange(lexeme)) {
                return new Token("decFloatLiteral",lexeme,fileManager.getCurrentLineNumber());
            } else {
                throw new LexicalException(lexeme,fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber()," excede el rango de valor para un float");
            }
        }
    }
    private Token decFloatAfterPeriod() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            nextCharacter();
            return decFloatAfterPeriod();
        } else if (currentChar == 'e' || currentChar == 'E') {
            updateLexeme();
            nextCharacter();
            return decFloatReadingExponentSignState();
        } else {
            return new Token("decFloatLiteral",lexeme,fileManager.getCurrentLineNumber());
        }
    }
    private Token decFloatReadingExponentSignState() throws LexicalException{
        if (currentChar == '+' || currentChar == '-') {
            updateLexeme();
            nextCharacter();
            return decFloatReadingExponentFirstDigitState();
        } else if (Character.isDigit(currentChar)) {
            return decFloatAfterExponent();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber(),"falta especificar el exponente del float");
        }
    }
    private Token decFloatReadingExponentFirstDigitState() throws LexicalException {
        if (Character.isDigit(currentChar)) {
            updateLexeme();
            nextCharacter();
            return decFloatAfterExponent();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber(), "se necesita especificar con digitos el exponente");
        }
    }
    private boolean isInFloatRange(String stringFloat) {
        float floatToTest = Float.parseFloat(stringFloat);
        boolean inRange = false;
        if (MIN_FLOAT_VALUE <= floatToTest && floatToTest  <= MAX_FLOAT_VALUE) {
            inRange = true;
        }
        return inRange;
    }
    private Token possibleComment() throws LexicalException {
        if (currentChar == '/') {
            int currentLine = fileManager.getCurrentLineNumber();
            while (currentLine == fileManager.getCurrentLineNumber()) {
                nextCharacter();
            }
            return nextToken();
        } else if (currentChar == '*') {
            char prevChar = ' ';
            int lastColumn = 0;
            updateLexeme();
            while (prevChar != '*' || currentChar != '/') {
                prevChar = currentChar;
                nextCharacter();
                if (fileManager.isEOF()) {
                    throw new LexicalException(lexeme,fileManager.getCurrentLineNumber()-1,lastColumn,"comentario multilinea nunca cerrado");
                }
                lastColumn = fileManager.getCurrentColumnNumber();
            }
            nextCharacter();
            return nextToken();
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
        if ( c == '%' || c == '*'){
            toReturn = true;
        }
        return toReturn;
    }

    Token punctuationState() {
        return new Token(getPunctuationId(),lexeme, fileManager.getCurrentLineNumber());
    }

    Token endOfFileState () {
        return new Token("EOF", lexeme, fileManager.getCurrentLineNumber());
    }

    Token idClassState() {
        if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
            updateLexeme();
            nextCharacter();
            return idClassState();
        } else {
            return new Token("idClase",lexeme,fileManager.getCurrentLineNumber());
        }
    }

    Token idMetVarState() {
        if ((Character.isLetterOrDigit(currentChar) || currentChar == '_') && reservedWords.get(lexeme) == null) {
            updateLexeme();
            nextCharacter();
            return idMetVarState();
        } else if (reservedWords.get(lexeme) != null) {
            String id = reservedWords.get(lexeme);
            return new Token(id,lexeme, fileManager.getCurrentLineNumber());
        } else {
            return new Token("idMetVar",lexeme,fileManager.getCurrentLineNumber());
        }
    }

    Token intLiteralState() throws LexicalException {
        //TODO fix , throw error when surpassing MAX_INT_LENGTH
        if (Character.isDigit(currentChar) && lexeme.length() < MAX_INT_LENGTH) {
            updateLexeme();
            nextCharacter();
            return intLiteralState();
        } else if (currentChar == '.'){
            updateLexeme();
            nextCharacter();
            return decFloatAfterPeriod();
        } else if (currentChar == 'e' || currentChar == 'E'){
            updateLexeme();
            nextCharacter();
            return decFloatReadingExponentSignState();
        } else {
            return new Token("intLiteral",lexeme,fileManager.getCurrentLineNumber());
        }
    }

    Token charLiteralStarterState() throws LexicalException {
        //TODO fix jump line for LexicalException
        if (currentChar != '\\' && currentChar != '\'' ) {
            updateLexeme();
            nextCharacter();
            return charLiteralFinishingState();
        } else if (currentChar == '\\'){
            updateLexeme();
            nextCharacter();
            updateLexeme();
            nextCharacter();
            return charLiteralFinishingState();
        }else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber(),"no es un caracter valido");
        }
    }
    Token charLiteralFinishingState() throws LexicalException {
        if (currentChar == '\'') {
            updateLexeme();
            nextCharacter();
            return new Token("charLiteral",lexeme, fileManager.getCurrentLineNumber());
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber(),"esperado cierre de caracter");
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
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber(),"no es un operador logico valido");
        }
    }

    Token opOrState() throws LexicalException {
        if (currentChar == '|') {
            updateLexeme();
            nextCharacter();
            return defaultOperatorStates();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber(),"no es un operador logico valido");
        }
    }

    Token stringStarterState() throws LexicalException {
        if (currentChar != '\n' && currentChar != '\\' && currentChar != '"' && currentChar != (char) 13 && currentChar != (char) -1){
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
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber(),"no es un caracter valido para un String o nunca se cierra el String");
        }
    }

    Token stringEscapeCharState() throws LexicalException {
        if (currentChar != '\n' && currentChar != (char) 13) {
            updateLexeme();
            nextCharacter();
            return stringStarterState();
        } else {
            throw new LexicalException(lexeme, fileManager.getCurrentLineNumber(), fileManager.getCurrentColumnNumber(), "no se permite un salto de linea en un String");
        }
    }

    Token stringFinishingState() {
        return new Token ("stringLiteral",lexeme, fileManager.getCurrentLineNumber());
    }

    private boolean isPunctuation(char c) {
        boolean toReturn = false;
        if ( c == '(' || c == ')' || c =='{' || c =='}' || c ==';' || c ==',' || c =='.')
            toReturn = true;
        return toReturn;
    }
    private String getPunctuationId() {
        String id = null;
        switch (lexeme.charAt(0)) {
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
            case "=":
                id = "assign";
                break;
            case "+=":
                id = "assignAdd";
                break;
            case "-=":
                id = "assignSub";
                break;
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
