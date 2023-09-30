package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;

import java.util.HashMap;

public class ClassST implements EntityST {
    private static final ConstructorST DEFAULT_CONSTRUCTOR = new ConstructorST();
    protected String className;
    protected Token declarationToken;
    protected Token implementedInterface;
    protected Token extendedClass;
    protected MethodST actualMethod;
    protected HashMap<String,AttributeST> attributes;
    protected HashMap<String,MethodST> methods;
    protected ConstructorST constructor;

    public ClassST(Token decToken,String className) {
        this.declarationToken = decToken;
        this.className = className;
        constructor = DEFAULT_CONSTRUCTOR;
        extendedClass = new Token("idClase","Object",0);

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
    public void setConstructor(Token token,ConstructorST constructor) throws SemanticException {
        if (this.constructor == DEFAULT_CONSTRUCTOR) {
            this.constructor = constructor;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"No se puede definir mas de un constructor por clase");
        }
    }

    public void inheritsFrom(Token extendedClass) throws SemanticException {
        if (implementedInterface == null) {
            this.extendedClass = extendedClass;
        } else {
            throw new SemanticException(extendedClass.getLexeme(),extendedClass.getLineNumber(),"No se puede implementar una interfaz y heredar de otra clase");
        }
    }
    public String getParentClassName() {
        return extendedClass.getLexeme();
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
            System.out.println("ABOUT TO INSERT METHOD "+method.getMethodName());
            if (isMethodMain(method)) {
                System.out.println("IS A MAIN METHOD");
                SymbolTable.getInstance().addMainMethod(method);
            }
            methods.put(token.getLexeme(),method);
            this.actualMethod = method;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un metodo en esta clase con la signatura "+token.getLexeme());
        }
    }
    private boolean isMethodMain(MethodST method) {
        System.out.println("Checking if "+method.getMethodName()+" is main method");
        System.out.println(method.getReturnType().toString());
        System.out.println((method.getMethodName().equals("main"))+" "+(method.getReturnType().toString().equals("void"))+" "+method.isStatic+" "+method.parameters.isEmpty());
        return (method.getMethodName().equals("main")) && (method.getReturnType().toString().equals("void")) && method.isStatic && method.parameters.isEmpty();
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
//        checkOverrideOfParentAttributes();
        checkOverrideOfParentMethod(this.getClassName());
    }
    private void checkOverrideOfParentMethod(String className) throws SemanticException {
    }


    public void consolidate() {

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
                throw new SemanticException(parentDeclarationToken.getLexeme(),parentDeclarationToken.getLineNumber(),"Herencia circular para la clase "+currentClassName);
            }
        }
    }
}
