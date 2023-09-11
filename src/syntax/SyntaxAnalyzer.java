package syntax;

import lexical.LexicalAnalyzer;
import lexical.LexicalException;
import lexical.Token;

import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, SyntaxException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        currentToken = lexicalAnalyzer.nextToken();
        Inicial();
    }
    void Inicial() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("ListaClases")) {
            ListaClases();
        }
    }
    void ListaClases() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("Clase")) {
            Clase();
        }
    }
    void Clase() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("ClaseConcreta")) {
            ClaseConcreta();
        } else if (isCurrentTokenOnFirstSetOf("Interface")) {
            Interface();
        }
    }
    void ClaseConcreta() throws LexicalException, SyntaxException {
        match("rs_class"); //TODO should we put identifier or lexeme
        match("idClass");
        GenericidadOpcional();
        HerenciaOpcional();
        match("openCurl");
        ListaMiembros();
        match("closeCurl");
    }
    void Interface() throws LexicalException, SyntaxException {
        match("rs_interface");
        match ("idClase");
        ExtiendeOpcional();
        HerenciaOpcional();
        match("openCurl");
        ListaEncabezados();
        match("closeCurl");
    }
    void HerenciaOpcional() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("HeredaDe")){
            HeredaDe();
        } else if (isCurrentTokenOnFirstSetOf("ImplementaA")) {
            ImplementaA();
        } else {

        }
    }
    void GenericidadOpcional() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("GenericidadOpcional")) {
            match("opLess");
            match("idClase");
            match("opGreater");
        } else {

        }
    }
    void HeredaDe() throws LexicalException, SyntaxException {
        match("rs_extends");
        match("idClase");
    }
    void ImplementaA() throws LexicalException, SyntaxException {
        match("rs_implements");
        match("idClase");
    }
    void ExtiendeOpcional() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("ExtiendeOpcional")) {
            match("rs_extends");
            match("idClase");
        } else {

        }
    }
    void ListaMiembros() {
        if (isCurrentTokenOnFirstSetOf("ListaMiembros")) {
            Miembro();
            ListaMiembros();
        } else {

        }
    }
    void ListaEncabezados() {

    }
    void Miembro() {

    }
    void AtributoMetodoOConstructor() {

    }
    void EncabezadoAtributoMetodo() {

    }
    void FinAtributoMetodo() {

    }
    void EncabezadoMetodo() {

    }
    void Constructor() {

    }
    void VisibilidadOpcional() {

    }
    void InicializacionOpcional() {

    }
    void TipoMiembro() {

    }
    void Tipo() {

    }
    void TipoPrimitivo() {

    }
    void EstaticoOpcional() {

    }
    void ArgsFormales() {

    }
    void ListaArgsFormalesOpcional() {

    }
    void ListaArgsFormales() {

    }
    void OtroArgFormal() {

    }
    void ArgFormal() {

    }
    void Bloque() {

    }
    void ListaSentencias() {

    }
    void Sentencia() {

    }
    void VarLocalConTipo() {

    }
    void VarLocal() {

    }
    void MulVarDec() {

    }
    void Return() {

    }
    void ExpresionOpcional() {

    }
    void If() {

    }
    void Else() {

    }
    void While() {

    }
    void Expresion() {

    }
    void ExpresionCompuesta() {

    }
    void RExpresionCompuesta() {

    }
    void OperadorBinario() {

    }
    void ExpresionBasica() {

    }
    void OperadorUnario() {

    }
    void Operando() {

    }
    void Literal() {

    }
    void Acceso() {

    }
    void Primario() {

    }
    void AccesoThis() {

    }
    void AccesoConstructor() {

    }
    void AccesoMetVar() {

    }
    void ArgsActualesOpcionales() {

    }
    void ExpresionParentizada() {

    }
    void AccesoMetodoEstatico() {

    }
    void ArgsActuales() {

    }
    void ListaExpsOpcional() {

    }
    void ListaExps() {

    }
    void ContinuaListaExps() {

    }
    void EncadenadoOpcional() {

    }


    private void match(String tokenName) throws LexicalException, SyntaxException {
        if (tokenName.equals(currentToken.getId())) {
                currentToken = lexicalAnalyzer.nextToken();
        } else {
            throw new SyntaxException(currentToken, tokenName);
        }
    }
    private boolean isCurrentTokenOnFirstSetOf(String noTerminal) {
        boolean is = false;
        if (firstSet(noTerminal).contains(currentToken.getId()))
            is = true;
        return is;
    }

    private List<String> firstSet(String noTerminal) {
        List<String> firstSetList = new ArrayList<String>();
        if (noTerminal.equals("ClaseConcreta")) {
            firstSetList.add("class");
        }
        return firstSetList;
    }
}
