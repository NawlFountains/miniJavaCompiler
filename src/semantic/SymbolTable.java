package semantic;

import lexical.SemanticException;
import lexical.Token;
import semantic.entities.ClassST;
import semantic.entities.EntityST;
import semantic.entities.InterfaceST;
import semantic.entities.MethodST;

import java.util.HashMap;

public class SymbolTable implements EntityST {
    protected HashMap<String,ClassST> classes;
    protected HashMap<String,InterfaceST> interfaces;
    protected ClassST actualClass;
    protected InterfaceST actualInterface;
    public static SymbolTable instance;
    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    private SymbolTable() {
        classes = new HashMap<>();
        interfaces = new HashMap<>();

        setupPredefinedClasses();
    }

    private void setupPredefinedClasses() {
        //TODO add Object, String and System
    }

    public void insertClass(Token token, ClassST classST) throws SemanticException {
        if (!classOrInterfaceExits(token.getLexeme())) {
            classes.put(token.getLexeme(),classST);
            actualClass = classST;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe una declaracion de clase o interfaz con el identificador "+token.getLexeme());
        }
    }
    public void insertInterface(Token token,InterfaceST interfaceST) throws SemanticException {
        if (!classOrInterfaceExits(token.getLexeme())) {
            interfaces.put(token.getLexeme(),interfaceST);
            actualInterface = interfaceST;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe una declaracion de clase o interfaz con el identificador "+token.getLexeme());
        }
    }
    private boolean classOrInterfaceExits(String classOrInterfaceName) {
        return (classes.get(classOrInterfaceName) != null || interfaces.get(classOrInterfaceName) != null);
    }
    public void checkDeclarations(){
        for (ClassST c : classes.values()) {
            c.checkDeclarations();
        }
        for (InterfaceST i : interfaces.values()) {
            i.checkDeclarations();
        }
    }
    public void isCorrectlyDeclared(){
        for (ClassST c : classes.values()) {
            c.isCorrectlyDeclared();
        }
        for (InterfaceST i : interfaces.values()) {
            i.isCorrectlyDeclared();
        }
    }
    public void consolidate(){
        for (ClassST c : classes.values()) {
            c.consolidate();
        }
        for (InterfaceST i : interfaces.values()) {
            i.consolidate();
        }
    }
}
