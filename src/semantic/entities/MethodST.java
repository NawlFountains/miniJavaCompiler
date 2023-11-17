package semantic.entities;

import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
import lexical.SemanticException;
import lexical.Token;
import semantic.ReferenceType;
import semantic.SymbolTable;

public class MethodST extends RoutineST implements EntityST {
    protected Token declarationToken;
    protected int offsetInVT;
    protected boolean isStatic;
    public MethodST(Token declarationToken,String methodName) {
        super(methodName);
        this.declarationToken = declarationToken;
        this.routineName = methodName;
    }
    public void setOffsetInVT(int offset) {
        offsetInVT = offset;
    }
    public int getOffsetInVT() {
        return offsetInVT;
    }
    public Token getDeclarationToken() {
        return declarationToken;
    }
    public void setStatic(Boolean isStatic) {
        this.isStatic = isStatic;
    }
    public boolean isStatic() {
        return isStatic;
    }
    public boolean existParameter(String parameterName) {
        return parameters.get(parameterName) != null;
    }

    public void checkDeclarations() throws SemanticException {
        if (returnType.getClass().toString().equals("class semantic.ReferenceType")) {
            checkReturnReferenceType((ReferenceType) returnType);
        }
        for (ParameterST p : parameters.values()) {
            p.checkDeclarations();
        }
    }
    private void checkReturnReferenceType(ReferenceType type) throws SemanticException {
        if (SymbolTable.getInstance().getClassWithName(type.toString()) == null && SymbolTable.getInstance().getInterfaceWithName(type.toString()) == null ) {
            throw new SemanticException(declarationToken.getLexeme(),declarationToken.getLineNumber(),"El tipo de retorno "+type.toString()+" no se encuentra declarado");
        }
    }

    @Override
    public void consolidate() {

    }
    public String toString() {
        String toReturn = returnType+" "+routineName+'(';
        for (ParameterST p : parameters.values()) {
            toReturn += p.toString()+" ,";
        }
        if (!parameters.isEmpty())
            toReturn = toReturn.substring(0,toReturn.length()-2);
        toReturn += ")";
        return toReturn;
    }
    public boolean equals(MethodST method) {
        return super.equals(method) && returnType.equals(method.getReturnType()) && (isStatic == method.isStatic);
    }
    public void generateCode() throws CodeGenerationException {
        //Check if its not a predefined method
        beginningCode();
        if (blockAST == null) //That mean its a predefined class
            generateCodeForPredefinedMethods();
        else {
            blockAST.generateCode();
            CodeGenerator.getInstance().addLine("FMEM "+blockAST.getAmountOfVariables());
            CodeGenerator.getInstance().addLine("STOREFP");
            if (SymbolTable.getInstance().getMainMethod().equals(this))
                 CodeGenerator.getInstance().addLine("RET 0");
            else if (isStatic())
                CodeGenerator.getInstance().addLine("RET "+(getParameterTypeList().size()));
            else
                CodeGenerator.getInstance().addLine("RET "+(getParameterTypeList().size()+1));
        }
        CodeGenerator.getInstance().addLine("");
    }

    private void generateCodeForPredefinedMethods() throws CodeGenerationException {
        String predefinedCodeToInsert = "";
        switch (routineName) {
            case "debugPrint":
            case "printI":
                predefinedCodeToInsert = "LOAD 3  ; Apila el parámetro\n" +
                        "IPRINT  ; Imprime el entero en el tope de la pila\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 1";
                break;
            case "printC":
                predefinedCodeToInsert = "LOAD 3  ; Apila el parámetro\n" +
                        "CPRINT  ; Imprime el char en el tope de la pila\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 1";
                break;
            case "printS":
                predefinedCodeToInsert = "LOAD 3  ; Apila el parámetro\n" +
                        "SPRINT  ; Imprime el string en el tope de la pila\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 1";
                break;
            case "printSln":
                predefinedCodeToInsert = "LOAD 3  ; Apila el parámetro\n" +
                        "SPRINT  ; Imprime el booleano en el tope de la pila\n" +
                        "PRNLN  ; Imprime el caracter de nueva línea\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 1";
                break;
            case "println":
                predefinedCodeToInsert = "PRNLN  ; Imprime el caracter de nueva línea\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 0";
                break;
            case "printCln":
                predefinedCodeToInsert = "LOAD 3  ; Apila el parámetro\n" +
                        "CPRINT  ; Imprime el char en el tope de la pila\n" +
                        "PRNLN  ; Imprime el caracter de nueva línea\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 1";
                break;
            case "read":
                predefinedCodeToInsert = "READ  ; Lee un valor entero\n" +
                        "PUSH 48  ; Por ASCII\n" +
                        "SUB\n" +
                        "STORE 3  ; Devuelve el valor entero leído, poniendo el tope de la pila en la locación reservada\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 0";
                break;
            case "printB":
                predefinedCodeToInsert = "LOAD 3  ; Apila el parámetro\n" +
                        "BPRINT  ; Imprime el booleano en el tope de la pila\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 1";
                break;
            case "printBln":
                predefinedCodeToInsert = "LOAD 3  ; Apila el parámetro\n" +
                        "BPRINT  ; Imprime el booleano en el tope de la pila\n" +
                        "PRNLN  ; Imprime el caracter de nueva línea\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 1";
                break;
            case "printIln":
                predefinedCodeToInsert = "LOAD 3  ; Apila el parámetro\n" +
                        "IPRINT  ; Imprime el entero en el tope de la pila\n" +
                        "PRNLN  ; Imprime el caracter de nueva línea\n" +
                        "STOREFP  ; Almacena el tope de la pila en el registro fp\n" +
                        "RET 1";
                break;
        }
        CodeGenerator.getInstance().addLine(predefinedCodeToInsert);
    }

    private void beginningCode() throws CodeGenerationException {
        CodeGenerator.getInstance().addLine(CodeGenerator.generateLabelForMethod(this)+": LOADFP");
        CodeGenerator.getInstance().addLine("LOADSP");
        CodeGenerator.getInstance().addLine("STOREFP");
    }
}
