package syntax;

import lexical.LexicalAnalyzer;
import lexical.LexicalException;
import lexical.Token;

import java.util.*;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;
    private boolean isStaticAccess = false;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, SyntaxException {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    public void startAnalysis() throws LexicalException, SyntaxException {
        currentToken = lexicalAnalyzer.nextToken();
        Inicial();
    }
    void Inicial() throws LexicalException, SyntaxException {
        ListaClases();
    }
    void ListaClases() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("Clase")) {
            Clase();
        } else if (isCurrentTokenOnFollowSetOf("ListaClases")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("Listaclases").toString());
        }
    }
    void Clase() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("ClaseConcreta")) {
            ClaseConcreta();
        } else if (isCurrentTokenOnFirstSetOf("Interface")) {
            Interface();
        }
    }

    void DeclaracionClases() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("DeclaracionClases")) {
            match("comma");
            match("idClase");
            DeclaracionClases();
        } else if (isCurrentTokenOnFollowSetOf("DeclaracionClases")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("DeclaraiconClases").toString());
        }
    }
    void ClaseConcreta() throws LexicalException, SyntaxException {
        match("rw_class");
        match("idClase");
        GenericidadOpcional();
        HerenciaOpcional();
        match("openCurl");
        System.out.println("Por ver lista de miembros");
        ListaMiembros();
        match("closeCurl");
    }
    void Interface() throws LexicalException, SyntaxException {
        match("rw_interface");
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
        } else if (isCurrentTokenOnFollowSetOf("HerenciaOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("HerenciaOpcional").toString());
        }
    }
    void GenericidadOpcional() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("GenericidadOpcional")) {
            match("opLess");
            match("idClase");
            DeclaracionClases();
            match("opGreater");
        } else if (isCurrentTokenOnFollowSetOf("GenericidadOpcional")) {

        } else {
            System.out.println("En genericidad opcional no encontramos que este en el siguiente, el follow set es de "+followSet("GenericidadOpcional").toString());
            throw new SyntaxException(currentToken, firstSet("GenericidadOpcional").toString());
        }
    }
    void HeredaDe() throws LexicalException, SyntaxException {
        match("rw_extends");
        match("idClase");
        GenericidadOpcional();
    }
    void ImplementaA() throws LexicalException, SyntaxException {
        match("rw_implements");
        match("idClase");
        GenericidadOpcional();
    }
    void ExtiendeOpcional() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("ExtiendeOpcional")) {
            match("rw_extends");
            match("idClase");
            GenericidadOpcional();
        } else if (isCurrentTokenOnFollowSetOf("ExtiendeOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ExtiendeOpcional").toString());
        }
    }
    void ListaMiembros() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("ListaMiembros")) {
            System.out.println("EN lista miembros");
            Miembro();
            ListaMiembros();
        } else if (isCurrentTokenOnFollowSetOf("ListaMiembros")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaMiembros").toString());
        }
    }
    void ListaEncabezados() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("EncabezadoMetodo")) {
            EncabezadoMetodo();
            ListaEncabezados();
        } else if (isCurrentTokenOnFollowSetOf("ListaEncabezados")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaEncabezados").toString());
        }
    }
    void Miembro() throws LexicalException, SyntaxException {
        VisibilidadOpcional();
        AtributoMetodoOConstructor();
    }
    void AtributoMetodoOConstructor() throws LexicalException, SyntaxException {
        if (currentToken.getId().contains("rw_static")) {
            match("rw_static");
            EncabezadoAtributoMetodo();
        } else if (isCurrentTokenOnFirstSetOf("EncabezadoAtributoMetodoConstructor")) {
            EncabezadoAtributoMetodoConstructor();
        }
    }

    private void EncabezadoAtributoMetodoConstructor() throws LexicalException, SyntaxException{
        if (currentToken.getId().contains("idClase")) {
            match("idClase");
            GenericidadOpcional();
            PosibleConstructor();
        } else if (isCurrentTokenOnFirstSetOf("TipoMiembroSinClase")) {
            System.out.println("Vemos que es parte de los tipos miembros sin clase");
            TipoMiembroSinClase();
            EncabezadoAtributoMetodoTipoDicho();
        }
    }

    private void TipoMiembroSinClase() throws LexicalException, SyntaxException {
        System.out.println("Primeros de tip oprmiitivo son "+firstSet("TipoPrimitivo").toString());
        if (isCurrentTokenOnFirstSetOf("TipoPrimitivo")) {
            TipoPrimitivo();
        } else if (currentToken.getId().contains("rw_void")) {
            match("rw_void");
        }
    }

    private void PosibleConstructor() throws LexicalException, SyntaxException {
        if (currentToken.getId().contains("idMetVar")) {
            match("idMetVar");
            FinAtributoMetodo();
        } else if (isCurrentTokenOnFirstSetOf("ArgsFormales")) {
            ArgsFormales();
            Bloque();
        }
    }

    private void EncabezadoAtributoMetodoTipoDicho() throws LexicalException, SyntaxException {
        match("idMetVar");
        FinAtributoMetodo();
    }

    void EncabezadoAtributoMetodo() throws LexicalException, SyntaxException {
        TipoMiembro();
        match("idMetVar");
        FinAtributoMetodo();
    }
    void FinAtributoMetodo() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("InicializacionOpcional") || currentToken.getId().contains("semiColon")) {
            InicializacionOpcional();
            match("semiColon");
        } else if (isCurrentTokenOnFirstSetOf("ArgsFormales")) {
            ArgsFormales();
            Bloque();
        }
    }
    void EncabezadoMetodo() throws LexicalException, SyntaxException {
        VisibilidadOpcional();
        EstaticoOpcional();
        TipoMiembro();
        match("idMetVar");
        ArgsFormales();
        match("semiColon");
    }
    void VisibilidadOpcional() throws LexicalException, SyntaxException {
        if (currentToken.getId().contains("rw_public")) {
            match("rw_public");
        } else if (currentToken.getId().contains("rw_private")) {
            match("rw_private");
        } else if (isCurrentTokenOnFollowSetOf("VisibilidadOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("VisibilidadOpcional").toString());
        }
    }
    void InicializacionOpcional() throws LexicalException, SyntaxException {
        if (currentToken.getId().contains("assign")) {
            match("assign");
            Expresion();
        } else if (isCurrentTokenOnFollowSetOf("InicializacionOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("InicializacionOpcional").toString());
        }
    }
    void TipoMiembro() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("Tipo")) {
            Tipo();
        } else if (currentToken.getId().contains("rw_void")) {
            match("rw_void");
        }
    }
    void Tipo() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("TipoPrimitivo")) {
            TipoPrimitivo();
        } else if (currentToken.getId().contains("idClase")) {
            match("idClase");
        }
    }
    void TipoPrimitivo() throws LexicalException, SyntaxException {
        if (currentToken.getId().contains("rw_boolean")) {
            match("rw_boolean");
        } else if (currentToken.getId().contains("rw_char")) {
            match("rw_char");
        } else if (currentToken.getId().contains("rw_int")) {
            match("rw_int");
        } else if (currentToken.getId().contains("rw_float")) {
            match("rw_float");
        }
    }
    void EstaticoOpcional() throws LexicalException, SyntaxException {
        if (currentToken.getId().contains("rw_static")) {
            match("rw_static");
        } else if (isCurrentTokenOnFollowSetOf("EstaticoOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("EstaticoOpcional").toString());
        }
    }
    void ArgsFormales() throws LexicalException, SyntaxException {
        match("openPar");
        ListaArgsFormalesOpcional();
        match("closePar");
    }
    void ListaArgsFormalesOpcional() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("ListaArgsFormales")) {
            ListaArgsFormales();
        } else if (isCurrentTokenOnFollowSetOf("ListaArgsFormalesOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaArgsFormalesOpcional").toString());
        }
    }
    void ListaArgsFormales() throws LexicalException, SyntaxException {
        ArgFormal();
        OtroArgFormal();
    }
    void OtroArgFormal() throws LexicalException, SyntaxException {
        if (currentToken.getId().contains("comma")) {
            match("comma");
            ListaArgsFormales();
        } else if (isCurrentTokenOnFollowSetOf("OtroArgFormal")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("OtroArgFormal").toString());
        }
    }
    void ArgFormal() throws LexicalException, SyntaxException {
        Tipo();
        match("idMetVar");
    }
    void Bloque() throws LexicalException, SyntaxException {
        match("openCurl");
        ListaSentencias();
        match("closeCurl");
    }
    void ListaSentencias() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("ListaSentencias")) {
            Sentencia();
            ListaSentencias();
        } else if (isCurrentTokenOnFollowSetOf("ListaSentencias")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaSentencias").toString());
        }
    }
    void Sentencia() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("Expresion")) {
            if (currentToken.getId().contains("idClase")) {
                match("idClase");
                if (currentToken.getId().contains("period")) {
                    match("period");
                    match("idMetVar");
                    ArgsActuales();
                    isStaticAccess = true;
                }
                if (isStaticAccess) {
                    //Si es acceso termina asi
                    EncadenadoOpcional();
                } else {
                    //Si es declaracion de variable
                    match("idMetVar");
                    DeclaracionVariableMultiple();
                    InicializacionOpcional();
                }
                isStaticAccess = false;
            } else {
                Expresion();
                match("semiColon");
            }
        } else if (isCurrentTokenOnFirstSetOf("VarLocal")) {
            VarLocal();
            match("semiColon");
        } else if (isCurrentTokenOnFirstSetOf("VarLocalConTipoPrimitivo")) {
            System.out.println("Se ecncontr que es una tipo para una declaracino de variable");
            VarLocalConTipoPrimitivo();
            match("semiColon");
        } else if (isCurrentTokenOnFirstSetOf("Return")) {
            Return();
            match("semiColon");
        } else if (isCurrentTokenOnFirstSetOf("If")) {
            If();
        } else if (isCurrentTokenOnFirstSetOf("While")) {
            While();
        } else if (isCurrentTokenOnFirstSetOf("Bloque")) {
            Bloque();
        } else if (currentToken.getId().contains("semiColon")) {
            match("semiColon");
        }
    }


    void VarLocalConTipoPrimitivo() throws LexicalException, SyntaxException {
        match(currentToken.getId());
        match("idMetVar");
        DeclaracionVariableMultiple();
        InicializacionOpcional();
    }


    void VarLocal() throws LexicalException, SyntaxException {
        match("rw_var");
        match("idMetVar");
        match("assign");
        ExpresionCompuesta();
    }
    void Return() throws LexicalException, SyntaxException {
        match("rw_return");
        ExpresionOpcional();
    }
    void ExpresionOpcional() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("Expresion")) {
            Expresion();
        } else if (isCurrentTokenOnFollowSetOf("ExpresionOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ExpresionOpcional").toString());
        }
    }
    void If() throws LexicalException, SyntaxException  {
        match("rw_if");
        match("openPar");
        Expresion();
        match("closePar");
        Sentencia();
        Else();
    }
    void Else() throws LexicalException, SyntaxException  {
        if (currentToken.getId().contains("rw_else")) {
            match("rw_else");
            Sentencia();
        } else if (isCurrentTokenOnFollowSetOf("Else")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("Else").toString());
        }
    }
    void While() throws LexicalException, SyntaxException  {
        match("rw_while");
        match("openPar");
        Expresion();
        match("closePar");
        Sentencia();
    }
    void Expresion() throws LexicalException, SyntaxException  {
        ExpresionCompuesta();
        InicializacionOpcional();
    }

    void ExpresionCompuesta() throws LexicalException, SyntaxException  {
        ExpresionBasica();
        RExpresionCompuesta();
    }
    void RExpresionCompuesta() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("OperadorBinario")) {
            OperadorBinario();
            ExpresionBasica();
            RExpresionCompuesta();
        } else if (isCurrentTokenOnFollowSetOf("RExpresionCompuesta")) {

        } else {
            System.out.println("Folow set of RExC is "+followSet("RExpresionCompuesta"));
            throw new SyntaxException(currentToken, firstSet("RExpresionCompuesta").toString());
        }
    }
    void OperadorBinario() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("OperadorBinario")) {
            match(currentToken.getId());
        }
    }
    void ExpresionBasica() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("OperadorUnario")) {
            OperadorUnario();
            Operando();
        } else if (isCurrentTokenOnFirstSetOf("Operando")) {
            Operando();
        }
    }
    void OperadorUnario() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("OperandorUnario")) {
            match(currentToken.getId());
        }
    }
    void Operando() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("Literal")) {
            Literal();
        } else if (isCurrentTokenOnFirstSetOf("Acceso")) {
            Acceso();
        }
    }
    void Literal() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("Literal")) {
            match(currentToken.getId());
        }
    }
    void Acceso() throws LexicalException, SyntaxException  {
        Primario();
        EncadenadoOpcional();
    }
    void Primario() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("AccesoThis")) {
            AccesoThis();
        } else if (isCurrentTokenOnFirstSetOf("AccesoMetVar")) {
            AccesoMetVar();
        } else if (isCurrentTokenOnFirstSetOf("AccesoConstructor")) {
            AccesoConstructor();
        } else if (isCurrentTokenOnFirstSetOf("AccesoMetodoEstatico")) {
            AccesoMetodoEstatico();
        } else if (isCurrentTokenOnFirstSetOf("ExpresionParentizada")) {
            ExpresionParentizada();
        }
    }
    void AccesoThis() throws LexicalException, SyntaxException  {
        match("rw_this");
    }
    void AccesoConstructor() throws LexicalException, SyntaxException  {
        match("rw_new");
        match("idClase");
        GenericidadOpcional();
        ArgsActuales();
    }
    void AccesoMetVar() throws LexicalException, SyntaxException  {
        match("idMetVar");
        ArgsActualesOpcionales();
    }
    void ArgsActualesOpcionales() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("ArgsActuales")) {
            ArgsActuales();
        } else if (isCurrentTokenOnFollowSetOf("ArgsActualesOpcionales")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ArgsActualesOpcionales").toString());
        }
    }
    void ExpresionParentizada() throws LexicalException, SyntaxException  {
        match("openPar");
        Expresion();
        match("closePar");
    }
    void AccesoMetodoEstatico() throws LexicalException, SyntaxException  {
        match("idClase");
        match("period");
        match("idMetVar");
        ArgsActuales();
    }

    void DeclaracionVariableMultiple() throws LexicalException, SyntaxException {
        if (currentToken.getId().contains("comma")) {
            match("comma");
            match("idMetVar");
            DeclaracionVariableMultiple();
        } else if (isCurrentTokenOnFollowSetOf("DeclaracionVariableMultiple")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("DeclaracionVariableMultiple").toString());
        }
    }

    void ArgsActuales() throws LexicalException, SyntaxException  {
        match("openPar");
        ListaExpsOpcional();
        match("closePar");
    }
    void ListaExpsOpcional() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("ListaExps")) {
            ListaExps();
        } else if (isCurrentTokenOnFollowSetOf("ListaExpsOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaExpsOpcional").toString());
        }
    }
    void ListaExps() throws LexicalException, SyntaxException  {
        Expresion();
        ContinuaListaExps();
    }
    void ContinuaListaExps() throws LexicalException, SyntaxException  {
        if (currentToken.getId().contains("comma")){
            match("comma");
            ListaExps();
        } else if (isCurrentTokenOnFollowSetOf("ContinuaListaExps")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ContinuaListaExps").toString());
        }
    }
    void EncadenadoOpcional() throws LexicalException, SyntaxException  {
        if (currentToken.getId().contains("period")) {
            match("period");
            match("idMetVar");
            ArgsActualesOpcionales();
            EncadenadoOpcional();
        } else if (isCurrentTokenOnFollowSetOf("EncadenadoOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("EncadenadoOpcional").toString());
        }
    }


    private void match(String tokenName) throws LexicalException, SyntaxException {
        System.out.println("currentToken id "+currentToken.getId()+" and tokenName "+ tokenName);
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
    private boolean isCurrentTokenOnFollowSetOf(String noTerminal) {
        boolean is = false;
        if (followSet(noTerminal).contains(currentToken.getId()))
            is = true;
        return is;
    }

    private Set<String> firstSet(String noTerminal) {
        Set<String> firstSet = new HashSet<String>();
        switch (noTerminal) {
            case "Inicial":
                firstSet = firstSet("ListaClases");
                break;
            case "ListaClases":
                firstSet = firstSet("Clase");
                break;
            case "Clase":
                firstSet = firstSet("ClaseConcreta");
                firstSet.addAll(firstSet("Interface"));
                break;
            case "DeclaracionClases":
                firstSet.add("comma");
                break;
            case "ClaseConcreta":
                firstSet.add("rw_class");
                break;
            case "Interface":
                firstSet.add("rw_interface");
                break;
            case "HerenciaOpcional":
                firstSet = firstSet("HeredaDe");
                firstSet.addAll(firstSet("ImplementaA"));
                break;
            case "GenericidadOpcional":
                firstSet.add("opLess");
                break;
            case "ExtiendeOpcional":
            case "HeredaDe":
                firstSet.add("rw_extends");
                break;
            case "ImplementaA":
                firstSet.add("rw_implements");
                break;
            case "ListaMiembros":
                firstSet = firstSet("Miembro");
                break;
            case "ListaEncabezados":
                firstSet = firstSet("EncabezadoMetodo");
                break;
            case "Miembro":
                firstSet = firstSet("VisibilidadOpcional");
                firstSet.addAll(firstSet("AtributoMetodoOConstructor"));
                break;
            case "AtributoMetodoOConstructor":
                firstSet.add("rw_static");
                firstSet.addAll(firstSet("EncabezadoAtributoMetodoConstructor"));
                break;
            case "EncabezadoAtributoMetodoConstructor":
                firstSet.add("idClase");
                firstSet.addAll(firstSet("TipoMiembroSinClase"));
                break;
            case "EncabezadoAtributoMetodoTipoDicho":
                firstSet.add("idMetVar");
                break;
            case "PosibleConstructor":
                firstSet.add("idMetVar");
                firstSet.addAll(firstSet("ArgsFormales"));
                break;
            case "EncabezadoAtributoMetodo":
                firstSet.addAll(firstSet("TipoMiembro"));
                break;
            case "FinAtributoMetodo":
                firstSet = firstSet("InicializacionOpcional");
                firstSet.add("semiColon");
                firstSet.addAll(firstSet("ArgsFormales"));
                break;
            case "EncabezadoMetodo":
                firstSet = firstSet("VisibilidadOpcional");
                firstSet.addAll(firstSet("EstaticoOpcional"));
                firstSet.addAll(firstSet("TipoMiembro"));
                break;
            case "TipoMiembroSinClase":
                firstSet.add("rw_void");
                firstSet.addAll(firstSet("TipoPrimitivo"));
                break;
            case "VisibilidadOpcional":
                firstSet.add("rw_public");
                firstSet.add("rw_private");
                break;
            case "InicializacionOpcional":
                firstSet.add("assign");
                break;
            case "TipoMiembro":
                firstSet = firstSet("Tipo");
                firstSet.add("rw_void");
                break;
            case "Tipo":
                firstSet = firstSet("TipoPrimitivo");
                firstSet.add("idClase");
                break;
            case "TipoPrimitivo":
                firstSet.add("rw_boolean");
                firstSet.add("rw_char");
                firstSet.add("rw_int");
                firstSet.add("rw_float");
                break;
            case "EstaticoOpcional":
                firstSet.add("rw_static");
                break;
            case "ArgsFormales":
                firstSet.add("openPar");
                break;
            case "ListaArgsFormalesOpcional":
                firstSet = firstSet("ListaArgsFormales");
                break;
            case "ListaArgsFormales":
                firstSet = firstSet("ArgFormal");
                break;
            case "OtroArgFormal":
                firstSet.add("comma");
                break;
            case "ArgFormal":
                firstSet = firstSet("Tipo");
                break;
            case "Bloque":
                firstSet.add("openCurl");
                break;
            case "ListaSentencias":
                firstSet = firstSet("Sentencia");
                break;
            case "Sentencia":
                firstSet = firstSet("Expresion");
                firstSet.addAll(firstSet("VarLocal"));
                firstSet.addAll(firstSet("VarLocalConTipoPrimitivo"));
                firstSet.addAll(firstSet("Return"));
                firstSet.addAll(firstSet("If"));
                firstSet.addAll(firstSet("While"));
                firstSet.addAll(firstSet("Bloque"));
                firstSet.add("semiColon");
                break;
            case "VarLocal":
                firstSet.add("rw_var");
                break;
            case "VarLocalConTipoPrimitivo" :
                firstSet = firstSet("TipoPrimitivo");
                break;
            case "DeclaracionVariableMultiple":
                firstSet.add("comma");
                break;
            case "Return":
                firstSet.add("rw_return");
                break;
            case "ExpresionOpcional":
                firstSet = firstSet("Expresion");
                break;
            case "If":
                firstSet.add("rw_if");
                break;
            case "Else":
                firstSet.add("rw_else");
                break;
            case "While":
                firstSet.add("rw_while");
                break;
            case "Expresion":
                firstSet = firstSet("ExpresionCompuesta");
                break;
            case "ExpresionCompuesta":
                firstSet = firstSet("ExpresionBasica");
                break;
            case "RExpresionCompuesta":
                firstSet = firstSet("OperadorBinario");
                break;
            case "OperadorBinario":
                firstSet.add("opOr");
                firstSet.add("opAnd");
                firstSet.add("opEq");
                firstSet.add("opNotEq");
                firstSet.add("opGreater");
                firstSet.add("opLess");
                firstSet.add("opLessEq");
                firstSet.add("opGreaterEq");
                firstSet.add("opAdd");
                firstSet.add("opSub");
                firstSet.add("opProd");
                firstSet.add("opDiv");
                firstSet.add("opIntDiv");
                break;
            case "ExpresionBasica":
                firstSet = firstSet("OperadorUnario");
                firstSet.addAll(firstSet("Operando"));
                break;
            case "OperadorUnario":
                firstSet.add("opAdd");
                firstSet.add("opSub");
                firstSet.add("opNot");
                break;
            case "Operando":
                firstSet = firstSet("Literal");
                firstSet.addAll(firstSet("Acceso"));
            case "Literal":
                firstSet.add("rw_null");
                firstSet.add("rw_true");
                firstSet.add("rw_false");
                firstSet.add("intLiteral");
                firstSet.add("charLiteral");
                firstSet.add("floatLiteral");
                firstSet.add("stringLiteral");
                break;
            case "Acceso":
                firstSet = firstSet("Primario");
                break;
            case "Primario":
                firstSet = firstSet("AccesoThis");
                firstSet.addAll(firstSet("AccesoMetVar"));
                firstSet.addAll(firstSet("AccesoConstructor"));
                firstSet.addAll(firstSet("AccesoMetodoEstatico"));
                firstSet.addAll(firstSet("ExpresionParentizada"));
                break;
            case "AccesoThis":
                firstSet.add("rw_this");
                break;
            case "AccesoConstructor":
                firstSet.add("rw_new");
                break;
            case "AccesoMetVar":
                firstSet.add("idMetVar");
                break;
            case "ArgsActualesOpcionales":
                firstSet = firstSet("ArgsActuales");
                break;
            case "ExpresionParentizada":
                firstSet.add("openPar");
                break;
            case "AccesoMetodoEstatico":
                firstSet.add("idClase");
                break;
            case "ArgsActuales":
                firstSet.add("openPar");
                break;
            case "ListaExpsOpcional":
                firstSet = firstSet("ListaExps");
                break;
            case "ListaExps":
                firstSet = firstSet("Expresion");
                break;
            case "ContinuaListaExps":
                firstSet.add("comma");
                break;
            case "EncadenadoOpcional":
                firstSet.add("period");
                break;
        }
        return firstSet;
    }
    private Set<String> followSet(String noTerminal) {
        Set<String> followSet = new HashSet<String>();
        switch (noTerminal) {
            case "ListaClase":
                break;
            case "DeclaracionClases":
                followSet.add("opGreater");
                followSet.add("idClase");
                break;
            case "ExtiendeOpcional":
            case "HerenciaOpcional":
                followSet.add("openCurl");
                break;
            case "ListaEncabezados":
            case "ListaMiembros":
            case "ListaSentencias":
                followSet.add("closeCurl");
                break;
            case "GenericidadOpcional":
                followSet.addAll(firstSet("HerenciaOpcional"));
                followSet.add("openPar");
                followSet.add("openCurl");
                followSet.add("idMetVar");
                break;
            case "VisibilidadOpcional":
                followSet.addAll(firstSet("AtributoMetodoOConstructor"));
                followSet.addAll(followSet("EstaticoOpcional"));
                break;
            case "InicializacionOpcional":
                followSet.add("semiColon");
                followSet.add("closePar");
                followSet.add("comma");
            case "ExpresionOpcional":
                //TODO see
                followSet.add("semiColon");
                break;
            case "EstaticoOpcional":
                followSet.addAll(firstSet("TipoMiembro"));
                break;
            case "OtroArgFormal":
            case "ListaArgsFormalesOpcional":
            case "ListaExpsOpcional":
            case "ContinuaListaExps":
                followSet.add("closePar");
                break;
            case "DeclaracionVariableMultiple":
            case "RExpresionCompuesta":
                followSet.addAll(firstSet("OperadorBinario"));
                followSet.addAll(firstSet("InicializacionOpcional"));
                followSet.addAll(followSet("InicializacionOpcional"));
                break;
            case "Else":
                followSet.addAll(firstSet("Sentencia"));
                break;
            case "ArgsActualesOpcionales":
                followSet.addAll(firstSet("EncadenadoOpcional"));
                followSet.addAll(followSet("EncadenadoOpcional"));
                break;
            case "EncadenadoOpcional":
                followSet.addAll(firstSet("OperadorBinario"));
                followSet.addAll(firstSet("InicializacionOpcional"));
                followSet.addAll(followSet("InicializacionOpcional"));
                followSet.add("closePar");
                break;
            default:
                System.out.println("ERROR NO SE ENCONTRO FOLLOW SET DE "+noTerminal);
        }
        return followSet;
    }
}
