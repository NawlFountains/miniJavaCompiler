package semantic.entities;

import lexical.SemanticException;
import lexical.Token;

import java.util.Hashtable;

public class ClassST implements EntityST {
    public String className;
    public String superClassName;
    protected MethodST actualMethod;
    public Hashtable<String,AttributeST> attributes;
    public Hashtable<String,MethodST> methods;

    public ClassST(String className) {
        this.className = className;
    }
    public String getClassName() {
        return className;
    }

    public void inheritsFrom(String superClassName) {
        this.superClassName = superClassName;
    }
    public void insertMethod(Token token, MethodST method) throws SemanticException {
        if (!existMethod(token.getLexeme())) {
            methods.put(token.getLexeme(),method);
            this.actualMethod = method;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un metodo en esta clase con la signatura "+token.getLexeme());
        }
    }

    private boolean existMethod(String methodName) {
        return (methods.get(methodName) != null);
    }

    @Override
    public void checkDeclarations() {

    }

    public void isCorrectlyDeclared() {
        for (AttributeST att : attributes.values()) {
            att.isCorrectlyDeclared();
        }
        for (MethodST met : methods.values()) {
            met.isCorrectlyDeclared();
        }
    }

    public void consolidate() {

    }
}
