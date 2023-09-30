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
    protected ClassST currentClass;
    protected InterfaceST currentInterface;
    protected MethodST currentMethod;
    public static SymbolTable instance;
    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    public void resetTable() {
        classes = new HashMap<>();
        interfaces = new HashMap<>();

        setupPredefinedClasses();
    }

    private SymbolTable() {
        classes = new HashMap<>();
        interfaces = new HashMap<>();

        setupPredefinedClasses();
    }

    private void setupPredefinedClasses() {
        //TODO add Object, String and System
    }
    public void setCurrentClass(ClassST currentClass) {
        this.currentClass = currentClass;
    }
    public ClassST getCurrentClass() {
        return currentClass;
    }
    public void setCurrentInterface(InterfaceST currentInterface) {
        this.currentInterface = currentInterface;
    }
    public InterfaceST getCurrentInterface() {
        return currentInterface;
    }
    public void setCurrentMethod(MethodST currentMethod) {
        this.currentMethod = currentMethod;
    }
    public MethodST getCurrentMethod() {
        return currentMethod;
    }

    public void insertClass(Token token, ClassST classST) throws SemanticException {
        if (!classOrInterfaceExits(token.getLexeme())) {
            classes.put(token.getLexeme(),classST);
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe una declaracion de clase o interfaz con el identificador "+token.getLexeme());
        }
    }
    public void insertInterface(Token token,InterfaceST interfaceST) throws SemanticException {
        if (!classOrInterfaceExits(token.getLexeme())) {
            interfaces.put(token.getLexeme(),interfaceST);
            currentInterface = interfaceST;
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
