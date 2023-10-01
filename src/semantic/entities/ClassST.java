package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import org.w3c.dom.Attr;
import semantic.SymbolTable;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ClassST implements EntityST {
    private ConstructorST DEFAULT_CONSTRUCTOR;
    protected String className;
    protected Token declarationToken;
    protected Token implementedInterface;
    protected Token extendedClass;
    protected MethodST actualMethod;
    protected boolean consolidated;
    protected HashMap<String,AttributeST> attributes;
    protected HashMap<String,MethodST> methods;
    protected ConstructorST constructor;

    public ClassST(Token decToken,String className) {
        this.declarationToken = decToken;
        this.className = className;
        DEFAULT_CONSTRUCTOR = new ConstructorST(className);
        constructor = DEFAULT_CONSTRUCTOR;
        extendedClass = new Token("idClase","Object",0);
        consolidated = false;

        attributes = new HashMap<>();
        methods = new HashMap<>();
    }
    public String getClassName() {
        return className;
    }
    public Token getDeclarationToken() {
        return declarationToken;
    }
    public Token getExtendedClassToken() {
        return extendedClass;
    }
    public boolean isConsolidated() { return consolidated;}
    public void setConstructor(Token token,ConstructorST constructor) throws SemanticException {
        if (this.constructor == DEFAULT_CONSTRUCTOR) {
            this.constructor = constructor;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"No se puede definir mas de un constructor por clase");
        }
    }

    public void inheritsFrom(Token extendedClass) throws SemanticException {
        if (implementedInterface == null || extendedClass.getLexeme().toString().equals("Object")) {
            this.extendedClass = extendedClass;
        } else {
            throw new SemanticException(extendedClass.getLexeme(),extendedClass.getLineNumber(),"No se puede implementar una interfaz y heredar de otra clase");
        }
    }
    public String getParentClassName() {
        return extendedClass.getLexeme();
    }
    public String getImplementedInterfaceName() {
        return implementedInterface.getLexeme();
    }
    public void implementsFrom(Token implementedInterface) throws SemanticException {
        System.out.println("ABOUT TO ADD INTERFACE "+implementedInterface.getLexeme());
        System.out.println("extended "+extendedClass);
        if (extendedClass.getLexeme() == "Object") {
            this.implementedInterface = implementedInterface;
        } else {
            throw new SemanticException(implementedInterface.getLexeme(),implementedInterface.getLineNumber(),"No se puede implementar una interfaz y heredar de otra clase");
        }
    }
    public void insertMethod(Token token, MethodST method) throws SemanticException {
        if (!existMethod(token.getLexeme())) {
            System.out.println("ABOUT TO INSERT METHOD "+method.getName());
            if (isMethodMain(method)) {
                SymbolTable.getInstance().addMainMethod(method);
            }
            methods.put(token.getLexeme(),method);
            this.actualMethod = method;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un metodo en esta clase con la signatura "+token.getLexeme());
        }
    }
    private boolean isMethodMain(MethodST method) {
        return (method.getName().equals("main")) && (method.getReturnType().toString().equals("void")) && method.isStatic && method.parameters.isEmpty();
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
    public void checkDeclarations() throws SemanticException {
        System.out.println("About to check circularInheritance");
        if (getParentClassName() != "Object") {
            circularInheritance(this.getClassName(),this.getClassName());
        }
        if (implementedInterface != null && SymbolTable.getInstance().getInterfaceWithName(implementedInterface.getLexeme()) == null) {
                throw new SemanticException(implementedInterface.getLexeme(),implementedInterface.getLineNumber(),"No esta declarada la interfaz "+implementedInterface.getLexeme()+" a implementar");
        }

        for (AttributeST att : attributes.values()) {
            att.checkDeclarations();
        }
        for (MethodST met : methods.values()) {
            met.checkDeclarations();
        }
    }
    public Collection<MethodST> getMethods() {
        return methods.values();
    }
    private void checkOverrideOfParentMethod(String className) throws SemanticException {
    }


    public void consolidate() throws SemanticException {
        // BC: we are in Object we dont have a superclass or interface
        if (!className.equals("Object")) {
        // RC: we differ if its a class or an interface
            if (implementedInterface != null && SymbolTable.getInstance().getInterfaceWithName(getImplementedInterfaceName()) != null) {
                System.out.println("Has interface time to consolidate");
                InterfaceST parentInterface = SymbolTable.getInstance().getInterfaceWithName(getImplementedInterfaceName());
                if (!parentInterface.isConsolidated()) {
                    parentInterface.consolidate();
                }

                //Check all interface methods are implemented
                Collection<MethodST> parentMethods = parentInterface.getMethods();
                for (MethodST m : parentMethods) {
                    System.out.println("Checking class "+className+" implements "+m.toString());
                    if (!checkIfMethodExist(m)) {
                        System.out.println("It doesnt");
                        throw new SemanticException(SymbolTable.getInstance().getEOF().getLexeme(),SymbolTable.getInstance().getEOF().getLineNumber(),"Nunca se implementa en la clase "+className+" el metodo "+m.getName()+" de la intefaz "+parentInterface.getInterfaceName());
                    }
                    System.out.println("It does");
                }
            }
            if (SymbolTable.getInstance().getClassWithName(getParentClassName()) != null) {
                ClassST parentClass = SymbolTable.getInstance().getClassWithName(getParentClassName());
                if (!parentClass.isConsolidated()) {
                    parentClass.consolidate();
                }
                //Bring all parent attributes
                Collection<AttributeST> parentAttributes = parentClass.getAttributes();
                for (AttributeST a: parentAttributes) {
                    if (!checkIfAttributeExists(a)) {
                        attributes.put(a.getAttributeName(),a);
                    }
                }

                //Bring all parent methods
                Collection<MethodST> parentMethods = parentClass.getMethods();
                for (MethodST m : parentMethods) {
                    //Checks
                    if (!checkIfMethodExist(m)) {
                        methods.put(m.getName(),m);
                    }
                }
            }

        }
        consolidated = true;
    }

    public Collection<AttributeST> getAttributes() {
        return attributes.values();
    }

    private void circularInheritance(String startClassName, String currentClassName) throws SemanticException {
        System.out.println("Checking circular inheritnace with start "+startClassName+" and current "+currentClassName);
        String parentClassName = SymbolTable.getInstance().getClassWithName(currentClassName).getParentClassName();
        ClassST parentClass = SymbolTable.getInstance().getClassWithName(parentClassName);
        System.out.println("Checking with parent class "+parentClassName);
        System.out.println("Parent class is null ? "+(parentClass == null));
        if (parentClass != null && parentClass.getClassName() != startClassName) {
            if (parentClass.getClassName() == "Object") {
                System.out.println("Parent class is Object");
            } else {
                circularInheritance(startClassName,parentClass.getParentClassName());
            }
        } else {
            Token parentDeclarationToken = SymbolTable.getInstance().getClassWithName(currentClassName).getExtendedClassToken();
            if (parentClass == null) {
                System.out.println("About to throw exception for parentClass null");
                throw new SemanticException(parentDeclarationToken.getLexeme(),parentDeclarationToken.getLineNumber(),"No existe la clase de la cual se hereda llamada "+parentDeclarationToken.getLexeme());
            } else {
                throw new SemanticException(startClassName,SymbolTable.getInstance().getClassWithName(startClassName).getDeclarationToken().getLineNumber(),"Herencia circular para la clase "+startClassName);
            }
        }
    }
    public String toString() {
        String toReturn = "Class Name : "+className+"\n";
        if (extendedClass != null) {
            toReturn += "Extends : "+getParentClassName()+"\n";
        }
        if (implementedInterface != null) {
            toReturn += "Implements : "+getImplementedInterfaceName()+"\n";
        }
        toReturn += "Attributes\n";
        for (AttributeST a : attributes.values()) {
            toReturn += a.toString()+"\n";
        }
        toReturn += "Methods\n";
        for (MethodST m : methods.values()) {
            toReturn += m.toString()+"\n";
        }
        toReturn += "Constructor\n";
        toReturn += constructor.toString();
        return toReturn;
    }
    private boolean checkIfMethodExist(MethodST method) throws SemanticException {
        boolean found = false;
        for (MethodST m : methods.values()) {
            if (m.getName().equals(method.getName())) {
                if (m.equals(method)) {
                    found = true;
                } else {
                    throw new SemanticException(m.getDeclarationToken().getLexeme(),m.getDeclarationToken().getLineNumber(),"El metodo "+method.toString()+" tiene el mismo nombre pero distinta signatura que el metodo que hereda "+method.toString());
                }
            }
        }
        return found;
    }
    private boolean checkIfAttributeExists(AttributeST attribute) throws SemanticException {
        boolean found = false;
        for (AttributeST a : attributes.values()) {
            if (a.getAttributeName().equals(attribute.getAttributeName())) {
                if (a.equals(attribute)) {
                    found = true;
                } else {
                    throw new SemanticException(a.getDeclarationToken().getLexeme(),a.getDeclarationToken().getLineNumber(),"El atributo "+attribute.toString()+" tiene el mismo nombre pero distinto tipo que el atributo que hereda "+attribute.toString());
                }
            }
        }
        return found;
    }
}
