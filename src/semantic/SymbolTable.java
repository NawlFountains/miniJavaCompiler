package semantic;

import lexical.SemanticException;
import lexical.Token;
import semantic.entities.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class SymbolTable implements EntityST {
    protected HashMap<String,ClassST> classes;
    protected HashMap<String,InterfaceST> interfaces;
    protected ClassST currentClass;
    protected InterfaceST currentInterface;
    protected MethodST currentMethod;
    public static SymbolTable instance;
    protected Collection<MethodST> mainMethods;
    protected Token EOF;

    private static class PredefinedClassBuilder {

        protected static ClassST getObjectClass() throws SemanticException {
            ClassST objectClass = new ClassST(new Token("idClase","Object",0),"Object");
            MethodST debugPrint = new MethodST(new Token("idMetVar","debugPrint",0),"debugPrint");

            ParameterST paramForDebug = new ParameterST(new Token("idMetVar","i",0),"i");
            paramForDebug.setParameterType(new PrimitiveType("int"));
            debugPrint.insertParameter(new Token("idMetVar","i",0),paramForDebug);

            debugPrint.setStatic(true);
            debugPrint.setReturnType(new PrimitiveType("void"));

            objectClass.insertMethod(new Token("idMetVar","debugPrint",0),debugPrint);
            return objectClass;
        }

        protected static ClassST getStringClass() throws SemanticException {
            ClassST stringClass = new ClassST(new Token("idClase","String",0),"String");
            stringClass.inheritsFrom(new Token("idClase","Object",0));
            return stringClass;
        }

        protected static ClassST getSystemClass() throws SemanticException {
            ClassST systemClass = new ClassST(new Token("idClase","System",0),"System");

            Collection<MethodST> methods = createMethodsForSystem();

            for (MethodST m : methods) {
                systemClass.insertMethod(new Token("idMetVar",m.getMethodName(),0),m);
            }
            systemClass.inheritsFrom(new Token("idClase","Object",0));
            return systemClass;
        }

        private static Collection<MethodST> createMethodsForSystem() throws SemanticException {
            Collection<MethodST> methodsToReturn = new HashSet<>();

            // static int read()
            MethodST read = new MethodST(new Token("idMetvar","read",0),"read");
            read.setStatic(true);
            read.setReturnType(new PrimitiveType("int"));

            methodsToReturn.add(read);

            // static void printB(boolean b)
            methodsToReturn.add(createVoidReturnMethodWithOneParameter("printB","boolean","b",false));

            // static void printC(char c)
            methodsToReturn.add(createVoidReturnMethodWithOneParameter("printC","char","c",false));

            // static void printI(int i)
            methodsToReturn.add(createVoidReturnMethodWithOneParameter("printI","int","i",false));

            // static void printS(String s)
            methodsToReturn.add(createVoidReturnMethodWithOneParameter("printS","String","s",true));

            // static void println()
            MethodST println = new MethodST(new Token("idMetVar","println",0),"println");
            println.setStatic(true);
            println.setReturnType(new PrimitiveType("void"));

            methodsToReturn.add(println);

            // static void printBln(boolean b)
            methodsToReturn.add(createVoidReturnMethodWithOneParameter("printBln","boolean","b",false));

            // static void printCln(char c)
            methodsToReturn.add(createVoidReturnMethodWithOneParameter("printCln","char","c",false));

            // static void printIln(int i)
            methodsToReturn.add(createVoidReturnMethodWithOneParameter("printIln","int","i",false));

            // static void printSln(String s)
            methodsToReturn.add(createVoidReturnMethodWithOneParameter("printSln","String","s",true));

            return methodsToReturn;
        }
        private static MethodST createVoidReturnMethodWithOneParameter(String methodName, String parameterType, String parameterName, Boolean isReferentialType) throws SemanticException {
            MethodST method = new MethodST(new Token("idMetVar",methodName,0),methodName);
            method.setStatic(true);
            method.setReturnType(new PrimitiveType("void"));

            ParameterST param = new ParameterST(new Token("idMetVar",parameterName,0),parameterName);
            if (isReferentialType) {
                param.setParameterType(new ReferenceType(parameterType));
            } else {
                param.setParameterType(new PrimitiveType(parameterType));
            }

            method.insertParameter(new Token("idMetVar",methodName,0),param);

            return method;
        }
    }
    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    public void resetTable() {
        classes = new HashMap<>();
        interfaces = new HashMap<>();
        mainMethods = new HashSet<>();

        setupPredefinedClasses();
    }

    private SymbolTable() {
        classes = new HashMap<>();
        interfaces = new HashMap<>();
        mainMethods = new HashSet<>();

        setupPredefinedClasses();
    }

    private void setupPredefinedClasses() {
        try {
            insertClass(new Token("idMetVar","Object",0),PredefinedClassBuilder.getObjectClass());
            insertClass(new Token("idMetVar","String",0),PredefinedClassBuilder.getStringClass());
            insertClass(new Token("idMetVar","System",0),PredefinedClassBuilder.getSystemClass());
        } catch (SemanticException e) {
            //In theory this could never happen if predefined classes are correctly set up
            new Exception("Problem with predefined classes");
        }
    }
    public void setEOFToken (Token eofToken) {
        this.EOF = eofToken;
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
    public void addMainMethod(MethodST mainMethod) {
            mainMethods.add(mainMethod);
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
    public ClassST getClassWithName(String className) {
        System.out.println("getClassWithName("+className+")");
        return classes.get(className);
    }
    public InterfaceST getInterfaceWithName(String interfaceName) {
        System.out.println("getInterfaceWithName("+interfaceName+")");
        return interfaces.get(interfaceName);
    }
    public void checkDeclarations() throws SemanticException {
        for (ClassST c : classes.values()) {
            System.out.println("Checking declaration of class "+c.getClassName());
            c.checkDeclarations();
        }
        for (InterfaceST i : interfaces.values()) {
            System.out.println("Checking declaration of interface "+i.getInterfaceName());
            i.checkDeclarations();
        }
        if (!hasMainMethod()) {
            throw new SemanticException(EOF.getLexeme(), EOF.getLineNumber(), "No se definio un metodo main");
        }
    }

    public boolean hasMainMethod() {
        return !mainMethods.isEmpty();
    }

    public void consolidate() throws SemanticException {
        for (ClassST c : classes.values()) {
            System.out.println("Consolidating "+c.getClassName());
            c.consolidate();
        }
        for (InterfaceST i : interfaces.values()) {
            System.out.println("Consolidating interface "+i.getInterfaceName());
            i.consolidate();
        }
    }
    public String toString(){
        String toReturn = "Classes\n";
        for (ClassST c : classes.values()) {
         toReturn += c.toString()+"\n";
        }
        toReturn += "Interfaces\n";
        for (InterfaceST i : interfaces.values()) {
            toReturn += i.toString()+"\n";
        }
        return toReturn;
    }
    public Token getEOF() {
        return EOF;
    }
}
