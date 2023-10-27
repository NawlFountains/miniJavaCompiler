package semantic.entities;

import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;

import java.util.*;

public class ClassST implements EntityST {
    private ConstructorST DEFAULT_CONSTRUCTOR;
    protected String className;
    protected Token declarationToken;
    protected List<Token> implementedInterfaces;
    protected List<Token> extendedClasses;
    protected MethodST actualMethod;
    protected boolean consolidated;
    protected HashMap<String,AttributeST> attributes;
    protected HashMap<String,MethodST> methods;
    protected ConstructorST constructor;

    public ClassST(Token decToken,String className) {
        this.declarationToken = decToken;
        this.className = className;
        extendedClasses = new ArrayList<>();
        implementedInterfaces = new ArrayList<>();
        DEFAULT_CONSTRUCTOR = new ConstructorST(className);
        constructor = DEFAULT_CONSTRUCTOR;
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
    public List<Token> getExtendedClassToken() {
        return extendedClasses;
    }
    public boolean isConsolidated() { return consolidated;}
    public void setConstructor(Token token,ConstructorST constructor) throws SemanticException {
        if (this.constructor == DEFAULT_CONSTRUCTOR) {
            if (!constructor.getName().equals(className)) {
                throw new SemanticException(token.getLexeme(),token.getLineNumber(),"El constructor debe tener el nombre de la clase , se esperaba "+className+" y se encontro "+token.getLexeme()+".");
            }
            this.constructor = constructor;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"No se puede definir mas de un constructor por clase");
        }
    }
    public ConstructorST getConstructor() {
        return constructor;
    }

    public void inheritsFrom(Token extendedClass) throws SemanticException {
        if (lexemeAlreadyOnList(extendedClasses,extendedClass)) {
            //If its already extendend we cant do it again
            throw new SemanticException(extendedClass.getLexeme(),extendedClass.getLineNumber(),"Ya se declaro la herencia de la clase "+extendedClass.getLexeme());
        }
        extendedClasses.add(extendedClass);
    }
    public List<String> getParentClassesNames() {
        List<String> toReturn = new ArrayList<>();
        for (Token t: extendedClasses) {
            toReturn.add(t.getLexeme());
        }
        return toReturn;
    }
    public List<String> getImplementedInterfacesNames() {
        List<String> toReturn = new ArrayList<>();
        for (Token t: implementedInterfaces) {
            toReturn.add(t.getLexeme());
        }
        return toReturn;
    }
    public void implementsFrom(Token implementedInterface) throws SemanticException {
            if (lexemeAlreadyOnList(implementedInterfaces,implementedInterface)) {
                //If its already extendend we cant do it again
                throw new SemanticException(implementedInterface.getLexeme(),implementedInterface.getLineNumber(),"Ya se declaro la implementacion de la interfaz "+implementedInterface.getLexeme());
            }
            implementedInterfaces.add(implementedInterface);
    }
    public void insertMethod(Token token, MethodST method) throws SemanticException {
        if (!existMethod(token.getLexeme())) {
            if (isMethodMain(method)) {
                SymbolTable.getInstance().addMainMethod(method);
            }
            methods.put(token.getLexeme(),method);
            method.setOwnerClass(this);
            this.actualMethod = method;
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un metodo en esta clase con la signatura "+token.getLexeme());
        }
    }
    private boolean isMethodMain(MethodST method) {
        return (method.getName().equals("main")) && (method.getReturnType().toString().equals("void")) && method.isStatic && method.parameters.isEmpty();
    }
    public void insertAttribute(Token token, AttributeST attribute) throws SemanticException {
        if (!existAttribute(token.getLexeme())) {
            attributes.put(token.getLexeme(),attribute);
        } else {
            throw new SemanticException(token.getLexeme(),token.getLineNumber(),"Ya existe un atributo en esta clase con el identificador "+token.getLexeme());
        }
    }

    private boolean existAttribute(String attributeName) {
        return (attributes.get(attributeName) != null); }

    private boolean existMethod(String methodName) {
        return (methods.get(methodName) != null);
    }

    @Override
    public void checkDeclarations() throws SemanticException {
        if (extendedClasses.size() >= 1 && extendedClasses.get(0).getLexeme() != "Object") {
            circularInheritance(this.getClassName(),this.getClassName());
        }
        if (!implementedInterfaces.isEmpty()) {
            for (Token t: implementedInterfaces) {
                if (SymbolTable.getInstance().getInterfaceWithName(t.getLexeme()) == null)
                    throw new SemanticException(t.getLexeme(),t.getLineNumber(),"No esta declarada la interfaz "+t.getLexeme()+" a implementar");
            }
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

    public void consolidate() throws SemanticException {
        // BC: we are in Object we dont have a superclass or interface
        if (!className.equals("Object")) {
        // RC: we differ if its a class or an interface
            if (!implementedInterfaces.isEmpty()) {
                for (Token t: implementedInterfaces) {
                    if (SymbolTable.getInstance().getInterfaceWithName(t.getLexeme()) != null) {
                        InterfaceST parentInterface = SymbolTable.getInstance().getInterfaceWithName(t.getLexeme());
                        if (!parentInterface.isConsolidated()) {
                            parentInterface.consolidate();
                        }

                        //Check all interface methods are implemented
                        Collection<MethodST> parentMethods = parentInterface.getMethods();
                        for (MethodST m : parentMethods) {
                            if (!checkIfMethodExist(m)) {
                                throw new SemanticException(m.getDeclarationToken().getLexeme(),m.getDeclarationToken().getLineNumber(),"Nunca se implementa en la clase "+className+" el metodo "+m.getName()+" de la intefaz "+parentInterface.getInterfaceName());
                            }
                        }

                    }
                }
            }
            for (Token t: extendedClasses) {
                if (SymbolTable.getInstance().getClassWithName(t.getLexeme()) != null) {
                    ClassST parentClass = SymbolTable.getInstance().getClassWithName(t.getLexeme());
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
        }
        consolidated = true;
    }

    public Collection<AttributeST> getAttributes() {
        return attributes.values();
    }

    private void circularInheritance(String startClassName, String currentClassName) throws SemanticException {
        List<String> parentClassNames = SymbolTable.getInstance().getClassWithName(currentClassName).getParentClassesNames();
        for (String parentClassName: parentClassNames) {
            ClassST parentClass = SymbolTable.getInstance().getClassWithName(parentClassName);
            if (parentClass != null && !parentClassName.equals(startClassName)) {
                if (parentClass.getClassName() == "Object") {
                } else {
                    circularInheritance(startClassName,parentClassName);
                }
            } else {
                Token parentDeclarationToken = SymbolTable.getInstance().getClassWithName(currentClassName).getDeclarationToken();
                if (parentClass == null) {
                    throw new SemanticException(parentClassName,parentDeclarationToken.getLineNumber(),"No existe la clase de la cual se hereda llamada "+parentClassName);
                } else {
                    throw new SemanticException(startClassName,SymbolTable.getInstance().getClassWithName(startClassName).getDeclarationToken().getLineNumber(),"Herencia circular para la clase "+startClassName);
                }
            }
        }
    }
    public String toString() {
        String toReturn = "Class Name : "+className+"\n";
        if (!extendedClasses.isEmpty()) {
            toReturn += "Extends : "+ getParentClassesNames().toString()+"\n";
        }
        if (!implementedInterfaces.isEmpty()) {
            toReturn += "Implements : "+ getImplementedInterfacesNames()+"\n";
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
    public String getASTStructure() {
        System.out.println("ClassST:getASTStructure:Start");
        String toReturn = "AST Methods :\n";
        for (MethodST m : methods.values()) {
            toReturn += m.getASTStructure()+"\n";
        }
        toReturn += "\n AST Constructor \n";
        toReturn += constructor.getASTStructure()+"\n";
        return toReturn;
    }
    private boolean checkIfMethodExist(MethodST method) throws SemanticException {
        boolean found = false;
        for (MethodST m : methods.values()) {
            if (m.getName().equals(method.getName())) {
                if (m.equals(method)) {
                    found = true;
                    break;
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
//                Checks of overriding atribute
//                if (a.equals(attribute)) {
//                    found = true;
//                } else {
//                    throw new SemanticException(a.getDeclarationToken().getLexeme(),a.getDeclarationToken().getLineNumber(),"El atributo "+attribute.toString()+" tiene el mismo nombre pero distinto tipo que el atributo que hereda "+attribute.toString());
//                }
                found = true;
            }
        }
        return found;
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
    public void checkSentences() throws SemanticException {
        for (MethodST m : methods.values()) {
            System.out.println("ClassST("+className+"):checkSentences():method:"+m.getName());
            //Skip predefined classes maybe not the best idea
            if (m.blockAST != null)
             m.blockAST.check();
        }
        if (constructor.blockAST != null)
            constructor.blockAST.check();
    }
}
