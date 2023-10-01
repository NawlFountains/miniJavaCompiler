package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InterfaceST implements EntityST {
    protected String interfaceName;
    protected List<Token> superInterfaces;
    protected Token declarationToken;
    protected MethodST actualMethod;
    protected HashMap<String,MethodST> methods;
    protected boolean consolidated;

    public InterfaceST(Token declarationToken,String interfaceName) {
        this.declarationToken = declarationToken;
        this.interfaceName = interfaceName;
        superInterfaces = new ArrayList<>();
        consolidated = false;
        methods = new HashMap<>();
    }

    public void inheritsFrom(Token superInterface) throws SemanticException {
        if (lexemeAlreadyOnList(superInterfaces,superInterface)) {
            throw new SemanticException(superInterface.getLexeme(),superInterface.getLineNumber(),"Ya se declaro la extension de la interfaz "+superInterface.getLexeme());
        } else {
            superInterfaces.add(superInterface);
        }
    }
    public String getInterfaceName() {
        return interfaceName;
    }
    public boolean isConsolidated() { return consolidated;}
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

    public void consolidate() throws SemanticException {
        if (!superInterfaces.isEmpty()) {
            for (Token t: superInterfaces) {
                if (SymbolTable.getInstance().getInterfaceWithName(t.getLexeme()) != null) {
                    InterfaceST parentInterface = SymbolTable.getInstance().getInterfaceWithName(t.getLexeme());
                    if (!parentInterface.isConsolidated()) {
                        parentInterface.consolidate();
                    }

                    //Check all interface methods are implemented
                    Collection<MethodST> parentMethods = parentInterface.getMethods();
                    for (MethodST m : parentMethods) {
                        checkNotOverrideMethod(m);
                        methods.put(m.getName(),m);
                    }
                }
            }
        }
        consolidated = true;
    }

    public String toString() {
        String toReturn = "Interface name : "+interfaceName+"\n";
        if (!superInterfaces.isEmpty()) {
                toReturn += "Extends : "+ superInterfaces.toString()+"\n";
        }
        toReturn += "Methods\n";
        for (MethodST m: methods.values()) {
            toReturn += m.toString()+"\n";
        }
        return toReturn;
    }
    public Collection<MethodST> getMethods() {
        return methods.values();
    }
    private void checkNotOverrideMethod(MethodST method) throws SemanticException {
        for (MethodST m : methods.values()) {
            if (m.getName().equals(method.getName())) {
                if (!m.equals(method)) {
                    throw new SemanticException(m.getDeclarationToken().getLexeme(),m.getDeclarationToken().getLineNumber(),"El metodo "+method.toString()+" tiene el mismo nombre pero distinta signatura que el metodo que hereda , el cual es "+method.toString());
                }
            }
        }
    }
    private boolean lexemeAlreadyOnList(List<Token> listOfTokens, Token tokenToFound) {
        boolean found = false;
        for (Token t: listOfTokens) {
            if (t.getLexeme() == tokenToFound.getLexeme()) {
                found = true;
                break;
            }
        }
        return found;
    }
}
