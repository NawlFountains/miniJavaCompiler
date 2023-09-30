package semantic.entities;

import lexical.SemanticException;
import lexical.Token;

import java.util.HashMap;
import java.util.Hashtable;

public class InterfaceST implements EntityST {
    protected String interfaceName;
    protected Token superInterface;
    protected Token declarationToken;
    protected MethodST actualMethod;
    protected HashMap<String,MethodST> methods;

    public InterfaceST(Token declarationToken,String interfaceName) {
        System.out.println("Interface "+interfaceName+" created");
        this.declarationToken = declarationToken;
        this.interfaceName = interfaceName;
        methods = new HashMap<>();
    }

    public void inheritsFrom(Token superInterface) {
        this.superInterface = superInterface;
    }
    public String getInterfaceName() {
        return interfaceName;
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
    public void checkDeclarations() throws SemanticException {
        for (MethodST m : methods.values()){
            if (m.isStatic) {
                throw new SemanticException(m.getDeclarationToken().getLexeme(),m.getDeclarationToken().getLineNumber(),"Una interfaz no puede tener un metodo estatico");
            }
            m.checkDeclarations();
        }
    }

    public void consolidate() {
        for (MethodST m : methods.values())
            m.consolidate();
    }
}
