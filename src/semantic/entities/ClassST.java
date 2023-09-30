package semantic.entities;

import lexical.SemanticException;
import lexical.Token;

import java.util.HashMap;
import java.util.Hashtable;

public class ClassST implements EntityST {
    protected String className;
    protected Token superClass;
    protected Token implementedInterface;
    protected MethodST actualMethod;
    protected HashMap<String,AttributeST> attributes;
    protected HashMap<String,MethodST> methods;
    protected ConstructorST constructor;

    public ClassST(String className) {
        this.className = className;
        constructor = null;

        attributes = new HashMap<>();
        methods = new HashMap<>();
    }
    public String getClassName() {
        return className;
    }
    public void setConstructor(Token token,ConstructorST constructor) throws SemanticException {
        if (this.constructor == null) {
            this.constructor = constructor;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"No se puede definir mas de un constructor por clase");
        }
    }

    public void inheritsFrom(Token superClass) {
        //TODO check that its a class
        this.superClass = superClass;
    }
    public void implementsFrom(Token implementedInterface) {
        //TODO check that it its an interface, it cant implement and extends at the same time
        this.implementedInterface = implementedInterface;
    }
    public void insertMethod(Token token, MethodST method) throws SemanticException {
        if (!existMethod(token.getLexeme())) {
            methods.put(token.getLexeme(),method);
            this.actualMethod = method;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un metodo en esta clase con la signatura "+token.getLexeme());
        }
    }
    public void insertAttribute(Token token, AttributeST attribute) throws SemanticException {
        if (!existMethod(token.getLexeme())) {
            attributes.put(token.getLexeme(),attribute);
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un atributo en esta clase con el identificador "+token.getLexeme());
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
