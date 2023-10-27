package syntax;

import ast.nodes.*;
import ast.nodes.access.*;
import lexical.LexicalAnalyzer;
import lexical.LexicalException;
import lexical.SemanticException;
import lexical.Token;
import semantic.PrimitiveType;
import semantic.ReferenceType;
import semantic.SymbolTable;
import semantic.Type;
import semantic.entities.*;

import java.util.*;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;
    private boolean isStaticAccess = false;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, SyntaxException {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    public void startAnalysis() throws LexicalException, SyntaxException, SemanticException {
        currentToken = lexicalAnalyzer.nextToken();
        Inicial();
    }
    void Inicial() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Inicial")) {
            ListaClases();
        } else {
            throw new SyntaxException(currentToken,firstSet("Inicial").toString());
        }
    }
    void ListaClases() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Clase")) {
            Clase();
            ListaClases();
        } else if (isCurrentTokenOnFollowSetOf("ListaClases")) {
            SymbolTable.getInstance().setEOFToken(currentToken);
        } else {
            throw new SyntaxException(currentToken, firstSet("Clase").toString());
        }
    }
    void Clase() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ClaseConcreta")) {
            ClaseConcreta();
        } else if (isCurrentTokenOnFirstSetOf("Interface")) {
            Interface();
        } else {
            Set<String> auxToException = firstSet("ClaseConcreta");
            auxToException.addAll(firstSet("Interface"));
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }

    void DeclaracionClases(boolean fromInterface) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("DeclaracionClases")) {
            match("comma");
            Token declaracionClase = currentToken;
            match("idClase");
            DeclaracionClases(fromInterface);
            if (fromInterface)
                SymbolTable.getInstance().getCurrentInterface().inheritsFrom(declaracionClase);
            else
                SymbolTable.getInstance().getCurrentClass().implementsFrom(declaracionClase);
        } else if (isCurrentTokenOnFollowSetOf("DeclaracionClases")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("DeclaracionClases").toString());
        }
    }
    void ClaseConcreta() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ClaseConcreta")) {
            match("rw_class");
            Token classToken = currentToken;
            match("idClase");
            ClassST clase = new ClassST(classToken,classToken.getLexeme());
            SymbolTable.getInstance().setCurrentClass(clase);
            GenericidadOpcional();
            HeredaOpcional();
            ImplementaOpcional();
            match("openCurl");
            ListaMiembros();
            match("closeCurl");
            SymbolTable.getInstance().insertClass(classToken,SymbolTable.getInstance().getCurrentClass());
        } else {
            throw new SyntaxException(currentToken, firstSet("ClaseConcreta").toString());
        }
    }
    void Interface() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Interface")) {
            match("rw_interface");
            Token interfaceToken = currentToken;
            match ("idClase");
            InterfaceST i = new InterfaceST(interfaceToken,interfaceToken.getLexeme());
            SymbolTable.getInstance().setCurrentInterface(i);
            ExtiendeOpcional();
            match("openCurl");
            ListaEncabezados();
            match("closeCurl");
            SymbolTable.getInstance().insertInterface(interfaceToken,i);
        } else {
            throw new SyntaxException(currentToken, firstSet("Interface").toString());
        }
    }
    void HerenciaOpcional() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("HeredaDe")){
            Token ancestro = HeredaDe();
            SymbolTable.getInstance().getCurrentClass().inheritsFrom(ancestro);
        } else if (isCurrentTokenOnFirstSetOf("ImplementaA")) {
            Token implementa = ImplementaA();
            SymbolTable.getInstance().getCurrentClass().implementsFrom(implementa);
        } else if (isCurrentTokenOnFollowSetOf("HerenciaOpcional")) {
            SymbolTable.getInstance().getCurrentClass().inheritsFrom(new Token("idClase","Object",0));
        } else {
            throw new SyntaxException(currentToken, firstSet("HerenciaOpcional").toString());
        }
    }
    void ImplementaOpcional() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ImplementaA")) {
            Token implementa = ImplementaA();
            SymbolTable.getInstance().getCurrentClass().implementsFrom(implementa);
        } else if (isCurrentTokenOnFollowSetOf("ImplementaOpcional")) {

        } else {
            throw new SyntaxException(currentToken, "rw_implements");
        }
    }
    void HeredaOpcional() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("HeredaDe")){
            Token ancestro = HeredaDe();
            SymbolTable.getInstance().getCurrentClass().inheritsFrom(ancestro);
        } else if (isCurrentTokenOnFollowSetOf("HeredaOpcional")) {
            SymbolTable.getInstance().getCurrentClass().inheritsFrom(new Token("idClase","Object",0));
        } else {
            throw new SyntaxException(currentToken, "rw_extends");
        }
    }
    void GenericidadOpcional() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("GenericidadOpcional")) {
            match("opLess");
            match("idClase");
            DeclaracionClases(false);
            match("opGreater");
        } else if (isCurrentTokenOnFollowSetOf("GenericidadOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("GenericidadOpcional").toString());
        }
    }
    void GenericidadVaciaOpcional() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("GenericidadVaciaOpcional")) {
            match("opLess");
            DeclaracionClasesOpcional();
            match("opGreater");
        } else if (isCurrentTokenOnFollowSetOf("GenericidadVaciaOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("GenericidadVaciaOpcional").toString());
        }
    }
    void DeclaracionClasesOpcional() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("DeclaracionClasesOpcional")) {
            match("idClase");
            DeclaracionClases(false);
        } else if (isCurrentTokenOnFollowSetOf("DeclaracionClasesOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("DeclaracionClasesOpcional").toString());
        }
    }
    Token HeredaDe() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("HeredaDe")) {
            match("rw_extends");
            Token tokenAncestro = currentToken;
            match("idClase");
            GenericidadOpcional();
            return tokenAncestro;
        } else {
            throw new SyntaxException(currentToken, firstSet("HeredaDe").toString());
        }
    }
    Token ImplementaA() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ImplementaA")){
            match("rw_implements");
            Token interfazImplementada = currentToken;
            match("idClase");
            GenericidadOpcional();
            DeclaracionClases(false);
            return interfazImplementada;
        } else {
            throw new SyntaxException(currentToken, firstSet("ImplementaA").toString());
        }
    }
    void ExtiendeOpcional() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ExtiendeOpcional")) {
            match("rw_extends");
            Token interfaceHeredada = currentToken;
            SymbolTable.getInstance().getCurrentInterface().inheritsFrom(interfaceHeredada);
            match("idClase");
            GenericidadOpcional();
            DeclaracionClases(true);
        } else if (isCurrentTokenOnFollowSetOf("ExtiendeOpcional")) {
        } else {
            throw new SyntaxException(currentToken, firstSet("ExtiendeOpcional").toString());
        }
    }
    void ListaMiembros() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ListaMiembros")) {
            Miembro();
            ListaMiembros();
        } else if (isCurrentTokenOnFollowSetOf("ListaMiembros")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaMiembros").toString());
        }
    }
    void ListaEncabezados() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("EncabezadoMetodo")) {
            EncabezadoMetodo();
            ListaEncabezados();
        } else if (isCurrentTokenOnFollowSetOf("ListaEncabezados")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaEncabezados").toString());
        }
    }
    void Miembro() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Miembro")){
            VisibilidadOpcional();
            AtributoMetodoOConstructor();
        } else {
            throw new SyntaxException(currentToken, firstSet("Miembro").toString());
        }
    }
    void AtributoMetodoOConstructor() throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("rw_static")) {
            match("rw_static");
            EncabezadoAtributoMetodo();
        } else if (isCurrentTokenOnFirstSetOf("EncabezadoAtributoMetodoConstructor")) {
            EncabezadoAtributoMetodoConstructor();
        } else {
            Set<String> auxToException = firstSet("EncabezadoAtributoMetodoConstructor");
            auxToException.add("rw_static");
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }

    private void EncabezadoAtributoMetodoConstructor() throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("idClase")) {
            Token decToken = currentToken;
            Type type = new ReferenceType(decToken.getLexeme());
            match("idClase");
            GenericidadOpcional();
            PosibleConstructor(decToken,type);
        } else if (isCurrentTokenOnFirstSetOf("TipoMiembroSinClase")) {
            Type type = TipoMiembroSinClase();
            EncabezadoAtributoMetodoTipoDicho(type);
        } else {
            Set<String> auxToException = firstSet("TipoMiembroSinClase");
            auxToException.add("idClase");
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }

    private Type TipoMiembroSinClase() throws LexicalException, SyntaxException {
        if (isCurrentTokenOnFirstSetOf("TipoPrimitivo")) {
            return TipoPrimitivo();
        } else if (currentToken.getId().contains("rw_void")) {
            match("rw_void");
            return new PrimitiveType("void");
        } else {
            Set<String> auxToException = firstSet("TipoPrimitivo");
            auxToException.add("rw_void");
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }

    private void PosibleConstructor(Token declarationToken,Type classType) throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("idMetVar")) {
            Token decToken = currentToken;
            match("idMetVar");
            FinAtributoMetodo(decToken,classType,false);
        } else if (isCurrentTokenOnFirstSetOf("ArgsFormales")) {
            //this mean is a constructor
            ConstructorST constructor = new ConstructorST(declarationToken.getLexeme());
            ArgsFormales(constructor);
            NodeBlock blockNode = Bloque();
            constructor.setAST(blockNode);
            blockNode.setRoutineEnvironment(constructor);
            SymbolTable.getInstance().getCurrentClass().setConstructor(declarationToken,constructor);
        } else {
            Set<String> auxToException = firstSet("ArgsFormales");
            auxToException.add("idMetVar");
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }

    private void EncabezadoAtributoMetodoTipoDicho(Type type) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("EncabezadoAtributoMetodoTipoDicho")) {
            Token idDec = currentToken;
            match("idMetVar");
            FinAtributoMetodo(idDec,type,false);
        } else {
            throw new SyntaxException(currentToken, firstSet("EncabezadoAtributoMetodoTipoDicho").toString());
        }
    }

    void EncabezadoAtributoMetodo() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("EncabezadoAtributoMetodo")){
            Type type = TipoMiembro();
            Token methodDeclaration = currentToken;
            match("idMetVar");
            FinAtributoMetodo(methodDeclaration,type,true);
        } else {
            throw new SyntaxException(currentToken, firstSet("EncabezadoAtributoMetodo").toString());
        }
    }
    void FinAtributoMetodo(Token nameDeclared, Type typeDeclared, Boolean isStatic ) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("DeclaracionVariableMultiple") ||  currentToken.getId().contains("semiColon")) {
            //TODO should we do optionals?
            DeclaracionVariableMultiple();
            InicializacionOpcional();

            AttributeST attr = new AttributeST(nameDeclared,nameDeclared.getLexeme());
            attr.setAttributeType(typeDeclared);
            attr.setStatic(isStatic);
            SymbolTable.getInstance().getCurrentClass().insertAttribute(nameDeclared,attr);
            match("semiColon");
        } else if (isCurrentTokenOnFirstSetOf("ArgsFormales")) {
            MethodST method = new MethodST(nameDeclared,nameDeclared.getLexeme());
            ArgsFormales(method);
            NodeBlock blockNode = Bloque();
            method.setAST(blockNode);
            method.setReturnType(typeDeclared);
            method.setStatic(isStatic);
            blockNode.setRoutineEnvironment(method);
            SymbolTable.getInstance().setCurrentMethod(method);
            SymbolTable.getInstance().getCurrentClass().insertMethod(nameDeclared,method);
        } else {
            Set<String> auxToException = firstSet("DeclaracionVariableMultiple");
            auxToException.addAll(firstSet("ArgsFormales"));
            auxToException.add("semiColon");
            throw new SyntaxException(currentToken,auxToException.toString());
        }
    }
    void EncabezadoMetodo() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("EncabezadoMetodo")) {
            VisibilidadOpcional();
            Boolean isStatic = EstaticoOpcional();
            Type returnType = TipoMiembro();
            Token methodName = currentToken;
            MethodST methodHeader = new MethodST(methodName,methodName.getLexeme());
            methodHeader.setReturnType(returnType);
            methodHeader.setStatic(isStatic);
            SymbolTable.getInstance().setCurrentMethod(methodHeader);
            match("idMetVar");
            ArgsFormales(methodHeader);
            match("semiColon");
            SymbolTable.getInstance().getCurrentInterface().insertMethod(methodName,methodHeader);
        } else {
            throw new SyntaxException(currentToken, firstSet("EncabezadoMetodo").toString());
        }
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
    NodeAssignment InicializacionOpcional(NodeCompoundExpression leftSide) throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("assign")) {
            Token declarationToken = currentToken;
            match("assign");
            NodeCompoundExpression rightSide = Expresion();
            NodeAssignment assignment = new NodeAssignment(leftSide,rightSide,declarationToken);
            return assignment;
        } else if (isCurrentTokenOnFollowSetOf("InicializacionOpcional")) {
            return null;
        } else {
            throw new SyntaxException(currentToken, firstSet("InicializacionOpcional").toString());
        }
    }
    void InicializacionOpcional() throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("assign")) {
            match("assign");
            Expresion();
        } else if (isCurrentTokenOnFollowSetOf("InicializacionOpcional")) {
        } else {
            throw new SyntaxException(currentToken, firstSet("InicializacionOpcional").toString());
        }
    }
    Type TipoMiembro() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Tipo")) {
            return Tipo();
        } else if (currentToken.getId().contains("rw_void")) {
            match("rw_void");
            return new PrimitiveType("void");
        } else {
            Set<String> auxToException = firstSet("Tipo");
            auxToException.add("rw_void");
            throw new SyntaxException(currentToken,auxToException.toString());
        }
    }
    Type Tipo() throws LexicalException, SyntaxException, SemanticException {
        Token aux = currentToken;
        if (isCurrentTokenOnFirstSetOf("TipoPrimitivo")) {
            TipoPrimitivo();
            return new PrimitiveType(aux.getLexeme());
        } else if (currentToken.getId().contains("idClase")) {
            match("idClase");
            GenericidadOpcional();
            return new ReferenceType(aux.getLexeme());
        } else {
            Set<String> auxToException = firstSet("TipoPrimitivo");
            auxToException.add("idClase");
            throw new SyntaxException(currentToken,auxToException.toString());
        }
    }
    Type TipoPrimitivo() throws LexicalException, SyntaxException {
        Token typeDec = currentToken;
        if (currentToken.getId().contains("rw_boolean")) {
            match("rw_boolean");
        } else if (currentToken.getId().contains("rw_char")) {
            match("rw_char");
        } else if (currentToken.getId().contains("rw_int")) {
            match("rw_int");
        } else if (currentToken.getId().contains("rw_float")) {
            match("rw_float");
        } else {
            Set<String> auxToException = firstSet("TipoPrimitivo");
            throw new SyntaxException(currentToken,auxToException.toString());
        }
        return new PrimitiveType(typeDec.getLexeme());
    }
    boolean EstaticoOpcional() throws LexicalException, SyntaxException {
        if (currentToken.getId().contains("rw_static")) {
            match("rw_static");
            return true;
        } else if (isCurrentTokenOnFollowSetOf("EstaticoOpcional")) {
            return false;
        } else {
            throw new SyntaxException(currentToken, firstSet("EstaticoOpcional").toString());
        }
    }
    void ArgsFormales(RoutineST callerRoutine) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ArgsFormales")){
            match("openPar");
            ListaArgsFormalesOpcional(callerRoutine);
            match("closePar");
        } else {
            throw new SyntaxException(currentToken, firstSet("ArgsFormales").toString());
        }
    }
    void ListaArgsFormalesOpcional(RoutineST callerRoutine) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ListaArgsFormales")) {
            ListaArgsFormales(callerRoutine);
        } else if (isCurrentTokenOnFollowSetOf("ListaArgsFormalesOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaArgsFormalesOpcional").toString());
        }
    }
    void ListaArgsFormales(RoutineST callerRoutine) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ListaArgsFormales")){
            ArgFormal(callerRoutine);
            OtroArgFormal(callerRoutine);
        } else {
            throw new SyntaxException(currentToken, firstSet("ListaArgsFormales").toString());
        }
    }
    void OtroArgFormal(RoutineST callerRoutine) throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("comma")) {
            match("comma");
            ListaArgsFormales(callerRoutine);
        } else if (isCurrentTokenOnFollowSetOf("OtroArgFormal")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("OtroArgFormal").toString());
        }
    }
    void ArgFormal(RoutineST callerRoutine) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ArgFormal")) {
            Type paramType = Tipo();
            Token decToken = currentToken;
            ParameterST param = new ParameterST(decToken,decToken.getLexeme());
            param.setParameterType(paramType);
            match("idMetVar");
            callerRoutine.insertParameter(decToken,param);
        } else {
            throw new SyntaxException(currentToken, firstSet("ArgFormal").toString());
        }
    }
    NodeBlock Bloque() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Bloque")) {
            match("openCurl");
            NodeBlock nodeBlock = new NodeBlock(true);
            ListaSentencias(nodeBlock);
            match("closeCurl");
            return nodeBlock;
        } else {
            throw new SyntaxException(currentToken, firstSet("Bloque").toString());
        }
    }
    NodeBlock Bloque(NodeBlock parentBlock) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Bloque")) {
            match("openCurl");
            NodeBlock nodeBlock = new NodeBlock(false);
            nodeBlock.addParentBlock(parentBlock);
            ListaSentencias(nodeBlock);
            match("closeCurl");
            return nodeBlock;
        } else {
            throw new SyntaxException(currentToken, firstSet("Bloque").toString());
        }
    }
    void ListaSentencias(NodeBlock nodeBlock) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ListaSentencias")) {
            NodeSentence sentence = Sentencia(nodeBlock);
            if (sentence != null) {
                nodeBlock.addSentence(sentence);
                sentence.addParentBlock(nodeBlock);
            }
            ListaSentencias(nodeBlock);
        } else if (isCurrentTokenOnFollowSetOf("ListaSentencias")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaSentencias").toString());
        }
    }
    NodeSentence Sentencia(NodeBlock parentBlock) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Expresion")) {
            if (currentToken.getId().contains("idClase")) {
                NodeAccessStaticMethod accessStaticMethod = null;
                Token idClassToken = currentToken;
                match("idClase");
                //TODO fix optional breaking captured
                if (currentToken.getId().contains("period")) {
                    match("period");
                    Token methodToken = currentToken;
                    match("idMetVar");

                    accessStaticMethod = new NodeAccessStaticMethod(idClassToken,methodToken);
                    ArgsActuales(accessStaticMethod);
                    isStaticAccess = true;
                }
                if (isStaticAccess) {
                    //Si es acceso termina asi
                    PrimerEncadenadoOpcional(accessStaticMethod);
                    return accessStaticMethod;
                } else {
                    //Si es declaracion de variable
                    match("idMetVar");
                    DeclaracionVariableMultiple();
                    InicializacionOpcional();
                }
                isStaticAccess = false;
                return null;
            } else {
                NodeCompoundExpression nodeExpression = Expresion();
                match("semiColon");
                return nodeExpression;
            }
        } else if (isCurrentTokenOnFirstSetOf("VarLocal")) {
            NodeVariableLocal nodeVar = VarLocal();
            match("semiColon");
            return nodeVar;
        } else if (isCurrentTokenOnFirstSetOf("VarLocalConTipoPrimitivo")) {
            NodeVariableLocal nodeVarPrimitive = VarLocalConTipoPrimitivo();
            match("semiColon");
            return nodeVarPrimitive;
        } else if (isCurrentTokenOnFirstSetOf("Return")) {
            NodeReturn nodeReturn = Return();
            match("semiColon");
            return nodeReturn;
        } else if (isCurrentTokenOnFirstSetOf("If")) {
            return If(parentBlock);
        } else if (isCurrentTokenOnFirstSetOf("While")) {
            return While(parentBlock);
        } else if (isCurrentTokenOnFirstSetOf("Bloque")) {
            return Bloque(parentBlock);
        } else if (currentToken.getId().contains("semiColon")) {
            match("semiColon");
            return null;
        } else {
            Set<String> auxToException = firstSet("Expresion");
            auxToException.addAll(firstSet("VarLocal"));
            auxToException.addAll(firstSet("VarLocalConTipoPrimitivo"));
            auxToException.addAll(firstSet("Return"));
            auxToException.addAll(firstSet("If"));
            auxToException.addAll(firstSet("While"));
            auxToException.addAll(firstSet("Bloque"));
            auxToException.add("semiColon");
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }


    NodeVariableLocal VarLocalConTipoPrimitivo() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("VarLocalConTipoPrimitivo")) {
            Type variableType = new PrimitiveType(currentToken.getId().substring(3,currentToken.getId().length()));
            match(currentToken.getId());
            currentToken.getLexeme();
            NodeVariableLocal nodeVariableLocal = new NodeVariableLocal(currentToken,variableType);
            match("idMetVar");
            //TODO optional breaking stuff
            DeclaracionVariableMultiple();

            NodeAssignment possibleAssignment = InicializacionOpcional(nodeVariableLocal);
//            if (possibleAssignment == null) {
//                return nodeVariable;
//            } else {
//                return NodeAssignment;
//            }
            return nodeVariableLocal;
        } else {
            throw new SyntaxException(currentToken, firstSet("VarLocalConTipoPrimitivo").toString());
        }
    }


    NodeVariableLocal VarLocal() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("VarLocal")) {
            match("rw_var");
            Token idMetVarToken = currentToken;
            match("idMetVar");
            match("assign");
            NodeCompoundExpression assignmentExpression = ExpresionCompuesta();
            NodeVariableLocal nodeVariableLocal = new NodeVariableLocal(idMetVarToken,assignmentExpression);
            return nodeVariableLocal;
        } else {
            Set<String> auxToException = firstSet("ClaseConcreta");
            auxToException.addAll(firstSet("Interface"));
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }
    NodeReturn Return() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Return")) {
            Token declarationToken = currentToken;
            match("rw_return");
            //Check optional
            NodeReturn nodeReturn = new NodeReturn(declarationToken);
            NodeCompoundExpression optionalExpression = ExpresionOpcional();
            if (optionalExpression != null) {
                nodeReturn = new NodeReturn(optionalExpression,declarationToken);
            }
            return nodeReturn;
        } else {
            throw new SyntaxException(currentToken, firstSet("Return").toString());
        }
    }
    NodeCompoundExpression ExpresionOpcional() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Expresion")) {
            return Expresion();
        } else if (isCurrentTokenOnFollowSetOf("ExpresionOpcional")) {
            return null;
        } else {
            throw new SyntaxException(currentToken, firstSet("ExpresionOpcional").toString());
        }
    }
    NodeIf If(NodeBlock nodeBlock) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("If")) {
            Token declarationToken = currentToken;
            match("rw_if");
            match("openPar");
            NodeCompoundExpression conditionalExpression = Expresion();
            match("closePar");
            NodeSentence thenSentence = Sentencia(nodeBlock);
            NodeIf nodeIf = new NodeIf(conditionalExpression,thenSentence,declarationToken);
            NodeElse possibleElse = Else(nodeBlock);
            if (possibleElse != null)
                nodeIf.addElse(possibleElse);
            return nodeIf;
        } else {
            throw new SyntaxException(currentToken, firstSet("If").toString());
        }
    }
    NodeElse Else(NodeBlock nodeBlock) throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("rw_else")) {
            match("rw_else");
            NodeSentence elseSentence = Sentencia(nodeBlock);
            return new NodeElse(elseSentence);
        } else if (isCurrentTokenOnFollowSetOf("Else")) {
            return null;
        } else {
            throw new SyntaxException(currentToken, firstSet("Else").toString());
        }
    }
    NodeWhile While(NodeBlock nodeBlock) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("While")) {
            Token declarationToken = currentToken;
            match("rw_while");
            match("openPar");
            NodeCompoundExpression conditionalExpression = Expresion();
            match("closePar");
            NodeSentence whileSentence = Sentencia(nodeBlock);
            NodeWhile nodeWhile = new NodeWhile(conditionalExpression,whileSentence,declarationToken);
            return nodeWhile;
        } else {
            throw new SyntaxException(currentToken, firstSet("While").toString());
        }
    }
    NodeCompoundExpression Expresion() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Expresion")) {
            NodeCompoundExpression nodeCompoundExp = ExpresionCompuesta();
            NodeAssignment possibleAssignment = InicializacionOpcional(nodeCompoundExp);
            if (possibleAssignment == null) {
                return nodeCompoundExp;
            } else {
                return possibleAssignment;
            }
        } else {
            throw new SyntaxException(currentToken, firstSet("Expresion").toString());
        }
    }

    NodeCompoundExpression ExpresionCompuesta() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ExpresionCompuesta")) {
            NodeCompoundExpression basicExpression = ExpresionBasica();
            NodeBinaryExpression leftOverExpression = RExpresionCompuesta(basicExpression);
            if (leftOverExpression == null) {
                return basicExpression;
            } else {
                return leftOverExpression;
            }
        } else {
            throw new SyntaxException(currentToken, firstSet("ExpresionCompuesta").toString());
        }
    }
    NodeBinaryExpression RExpresionCompuesta(NodeCompoundExpression leftSide) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("OperadorBinario")) {
            NodeOperand operand = OperadorBinario();
            NodeCompoundExpression rightSide = ExpresionBasica();
            NodeBinaryExpression toReturn = new NodeBinaryExpression(leftSide,operand,rightSide);
            NodeCompoundExpression leftOver = RExpresionCompuesta(rightSide);
            if (leftOver == null)
                return toReturn;
            else
                return new NodeBinaryExpression(leftSide,operand,leftOver);
        } else if (isCurrentTokenOnFollowSetOf("RExpresionCompuesta")) {
            return null;
        } else {
            throw new SyntaxException(currentToken, firstSet("RExpresionCompuesta").toString());
        }
    }
    NodeOperand OperadorBinario() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("OperadorBinario")) {
            NodeOperand operand = new NodeOperand(currentToken);
            match(currentToken.getId());
            return operand;
        } else {
            throw new SyntaxException(currentToken, firstSet("OperadorBinario").toString());
        }
    }
    NodeCompoundExpression ExpresionBasica() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("OperadorUnario")) {
            NodeOperand unaryOperand = OperadorUnario();
            NodeCompoundExpression operand = Operando();
            NodeUnaryExpression nodeUnaryExp = new NodeUnaryExpression(operand);
            nodeUnaryExp.addUnaryOperand(unaryOperand);
            return nodeUnaryExp;
        } else if (isCurrentTokenOnFirstSetOf("Operando")) {
            NodeCompoundExpression operand = Operando();
            return operand;
        } else {
            Set<String> auxToException = firstSet("OperadorUnario");
            auxToException.addAll(firstSet("Operando"));
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }
    NodeOperand OperadorUnario() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("OperadorUnario")) {
            NodeOperand nodeOperand = new NodeOperand(currentToken);
            match(currentToken.getId());
            return nodeOperand;
        } else {
            throw new SyntaxException(currentToken, firstSet("OperadorUnario").toString());
        }
    }
    NodeCompoundExpression Operando() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Literal")) {
            return Literal();
        } else if (isCurrentTokenOnFirstSetOf("Acceso")) {
            return Acceso();
        } else {
            Set<String> auxToException = firstSet("Literal");
            auxToException.addAll(firstSet("Acceso"));
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }
    NodeLiteral Literal() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("Literal")) {
            NodeLiteral literal = new NodeLiteral(currentToken);
            match(currentToken.getId());
            return literal;
        } else {
            throw new SyntaxException(currentToken, firstSet("Literal").toString());
        }
    }
    NodeCompoundExpression Acceso() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("Acceso")) {
            NodeCompoundExpression firstAccess = Primario();
            if (firstAccess instanceof NodeAccess) {
                PrimerEncadenadoOpcional((NodeAccess)firstAccess);
            }
            return firstAccess;
        } else {
            throw new SyntaxException(currentToken, firstSet("Acceso").toString());
        }
    }
    NodeCompoundExpression Primario() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("AccesoThis")) {
            return AccesoThis();
        } else if (isCurrentTokenOnFirstSetOf("AccesoMetVar")) {
            return AccesoMetVar();
        } else if (isCurrentTokenOnFirstSetOf("AccesoConstructor")) {
            return AccesoConstructor();
        } else if (isCurrentTokenOnFirstSetOf("AccesoMetodoEstatico")) {
            return AccesoMetodoEstatico();
        } else if (isCurrentTokenOnFirstSetOf("ExpresionParentizada")) {
            return ExpresionParentizada();
        } else {
            Set<String> auxToException = firstSet("AccesoThis");
            auxToException.addAll(firstSet("AccesoMetVar"));
            auxToException.addAll(firstSet("AccesoConstructor"));
            auxToException.addAll(firstSet("AccesoMetodoEstatico"));
            auxToException.addAll(firstSet("ExpresionParentizada"));
            throw new SyntaxException(currentToken, auxToException.toString());
        }
    }
    NodeAccessThis AccesoThis() throws LexicalException, SyntaxException  {
        if (isCurrentTokenOnFirstSetOf("AccesoThis")) {
            NodeAccessThis thisAccess = new NodeAccessThis(currentToken);
            match("rw_this");
            return thisAccess;
        } else {
            throw new SyntaxException(currentToken, firstSet("AccesoThis").toString());
        }
    }
    NodeAccessConstructor AccesoConstructor() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("AccesoConstructor")) {
            match("rw_new");
            Token idClassToken = currentToken;
            match("idClase");
            GenericidadVaciaOpcional();
            NodeAccessConstructor constructorNode = new NodeAccessConstructor(idClassToken);
            ArgsActuales(constructorNode);
            return constructorNode;
        } else {
            throw new SyntaxException(currentToken, firstSet("AccesoConstructor").toString());
        }
    }
    NodeAccessMetVar AccesoMetVar() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("AccesoMetVar")) {
            NodeAccessMetVar accessNode = new NodeAccessMetVar(currentToken);
            match("idMetVar");
            ArgsActualesOpcionales(accessNode);
            return  accessNode;
        } else {
            throw new SyntaxException(currentToken, firstSet("AccesoMetVar").toString());
        }
    }
    void ArgsActualesOpcionales(NodeAccess accessNode) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ArgsActuales")) {
            ArgsActuales(accessNode);
        } else if (isCurrentTokenOnFollowSetOf("ArgsActualesOpcionales")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ArgsActualesOpcionales").toString());
        }
    }
    NodeCompoundExpression ExpresionParentizada() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ExpresionParentizada")) {
            match("openPar");
            NodeCompoundExpression nodeExpression = Expresion();
            match("closePar");
            return nodeExpression;
        } else {
            throw new SyntaxException(currentToken, firstSet("ExpresionParentizada").toString());
        }
    }
    NodeAccessStaticMethod AccesoMetodoEstatico() throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("AccesoMetodoEstatico")) {
            Token receiverClass = currentToken;
            match("idClase");
            match("period");
            Token methodCalled = currentToken;
            match("idMetVar");
            NodeAccessStaticMethod staticCall= new NodeAccessStaticMethod(receiverClass,methodCalled);
            ArgsActuales(staticCall);
            return staticCall;
        } else {
            throw new SyntaxException(currentToken, firstSet("AccesoMetodoEstatico").toString());
        }
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

    void ArgsActuales(NodeAccess accessNode) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ArgsActuales")) {
            match("openPar");
            accessNode.isNotAttribute();
            ListaExpsOpcional(accessNode);
            match("closePar");
        } else {
            throw new SyntaxException(currentToken, firstSet("ArgsActuales").toString());
        }
    }
    void ListaExpsOpcional(NodeAccess accessNode) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ListaExps")) {
            ListaExps(accessNode);
        } else if (isCurrentTokenOnFollowSetOf("ListaExpsOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ListaExpsOpcional").toString());
        }
    }
    void ListaExps(NodeAccess accessNode) throws LexicalException, SyntaxException, SemanticException {
        if (isCurrentTokenOnFirstSetOf("ListaExps")) {
            NodeCompoundExpression argument = Expresion();
            accessNode.addArgument(argument);
            ContinuaListaExps(accessNode);
        } else {
            throw new SyntaxException(currentToken, firstSet("ListaExps").toString());
        }
    }
    void ContinuaListaExps(NodeAccess accessNode) throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("comma")){
            match("comma");
            ListaExps(accessNode);
        } else if (isCurrentTokenOnFollowSetOf("ContinuaListaExps")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("ContinuaListaExps").toString());
        }
    }
    void PrimerEncadenadoOpcional(NodeAccess ankorNode) throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("period")) {
            match("period");
            Token methodOrVarToken = currentToken;
            match("idMetVar");
            NodeChained nodeChained = new NodeChained(ankorNode,methodOrVarToken);
            ankorNode.addChainingNode(nodeChained);
            ArgsActualesOpcionales(nodeChained);
            EncadenadoOpcional(nodeChained);
        } else if (isCurrentTokenOnFollowSetOf("EncadenadoOpcional")) {

        } else {
            throw new SyntaxException(currentToken, firstSet("EncadenadoOpcional").toString());
        }
    }
    void EncadenadoOpcional(NodeChained ankorNode) throws LexicalException, SyntaxException, SemanticException {
        if (currentToken.getId().contains("period")) {
            match("period");
            Token methodOrVarToken = currentToken;
            match("idMetVar");
            NodeChained nodeChained = new NodeChained(ankorNode,methodOrVarToken);
            ankorNode.addChainedNode(nodeChained);
            ArgsActualesOpcionales(nodeChained);
            EncadenadoOpcional(nodeChained);
        } else if (isCurrentTokenOnFollowSetOf("EncadenadoOpcional")) {
        } else {
            throw new SyntaxException(currentToken, firstSet("EncadenadoOpcional").toString());
        }
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
            case "DeclaracionClasesOpcional":
                firstSet.add("idClase");
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
            case "GenericidadVaciaOpcional":
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
                firstSet.addAll(firstSet("DeclaracionVariableMultiple"));
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
                firstSet.add("assign");
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
                firstSet.add("opMul");
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
            case "DeclaracionClasesOpcional":
            case "DeclaracionClases":
                followSet.add("opGreater");
                followSet.add("idClase");
                followSet.add("openCurl");
                break;
            case "ExtiendeOpcional":
            case "HerenciaOpcional":
                followSet.add("openCurl");
                break;
            case "HeredaOpcional":
                followSet.add("openCurl");
                followSet.add("rw_implements");
                break;
            case "ListaEncabezados":
            case "ListaMiembros":
            case "ListaSentencias":
                followSet.add("closeCurl");
                break;
            case "GenericidadVaciaOpcional":
            case "GenericidadOpcional":
                followSet.addAll(firstSet("HerenciaOpcional"));
                followSet.add("comma");
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
                followSet.add("closeCurl");
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
            case "ListaClases":
                followSet.addAll(firstSet("Clase"));
                followSet.add("EOF");
                break;
            case "ImplementaOpcional":
                followSet.add("openCurl");
        }
        return followSet;
    }
}
