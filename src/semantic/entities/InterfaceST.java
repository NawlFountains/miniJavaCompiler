package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;

import java.util.Collection;
import java.util.HashMap;

public class InterfaceST implements EntityST {
    protected String interfaceName;
    protected Token superInterface;
    protected Token declarationToken;
    protected MethodST actualMethod;
    protected HashMap<String,MethodST> methods;
    protected boolean consolidated;

    public InterfaceST(Token declarationToken,String interfaceName) {
        System.out.println("Interface "+interfaceName+" created");
        this.declarationToken = declarationToken;
        this.interfaceName = interfaceName;
        consolidated = false;
        methods = new HashMap<>();
    }

    public void inheritsFrom(Token superInterface) {
        System.out.println("Por setear herencia a interfaz "+superInterface.getLexeme());
        this.superInterface = superInterface;
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
        if (superInterface != null && SymbolTable.getInstance().getInterfaceWithName(superInterface.getLexeme()) != null) {
            System.out.println("Has interface time to consolidate");
            InterfaceST parentInterface = SymbolTable.getInstance().getInterfaceWithName(superInterface.getLexeme());
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
        consolidated = true;
    }

    public String toString() {
        String toReturn = "Interface name : "+interfaceName+"\n";
        if (superInterface != null) {
            toReturn += "Extends : "+superInterface.getLexeme()+"\n";
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
}
