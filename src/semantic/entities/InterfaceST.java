package semantic.entities;

import lexical.SemanticException;
import lexical.Token;

import java.util.Hashtable;

public class InterfaceST implements EntityST {
    protected String interfaceName;
    protected Token superInterface;
    protected MethodST actualMethod;
    protected Hashtable<String,MethodST> methods;

    public InterfaceST(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void inheritsFrom(Token superInterface) {
        this.superInterface = superInterface;
    }
    public void insertMethod(Token token, MethodST method) throws SemanticException {
        if (!existMethod(token.getLexeme())) {
            if (!method.isStatic) {
                methods.put(token.getLexeme(),method);
                this.actualMethod = method;
            } else {
                throw new SemanticException(token.getLexeme(),token.getLineNumber(),"No se puede declarar un metodo estatico en una interfaz ");
            }
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un metodo en esta interfaz con la signatura "+token.getLexeme());
        }
    }

    private boolean existMethod(String methodName) {
        return (methods.get(methodName) != null);
    }
    public void checkDeclarations() {
        for (MethodST m : methods.values())
            m.checkDeclarations();
    }

    public void isCorrectlyDeclared() {
        for (MethodST m : methods.values())
            m.isCorrectlyDeclared();
    }

    public void consolidate() {
        for (MethodST m : methods.values())
            m.consolidate();
    }
}
