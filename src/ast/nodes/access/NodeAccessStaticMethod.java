package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeCompoundExpression;
import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;
import semantic.entities.ClassST;
import semantic.entities.MethodST;
import semantic.entities.RoutineST;

public class NodeAccessStaticMethod extends NodeAccess implements Node {
    protected Token methodToken;

    public NodeAccessStaticMethod(Token callerToken, Token methodToken) {
        super(callerToken);
        this.methodToken = methodToken;
    }

    @Override
    public void check() throws SemanticException {
        checkArgumentsList();
        ClassST receiverClass = SymbolTable.getInstance().getClassWithName(operandToken.getLexeme());
        boolean found = false;
        if (receiverClass == null) {
            throw new SemanticException(operandToken.getLexeme(), operandToken.getLineNumber(), "No existe la clase " + operandToken.getLexeme() + " referida.");
        }
        for (MethodST m : receiverClass.getMethods()) {
            if (methodToken.getLexeme().equals(m.getName())) {
                if (m.getParameterTypeList().size() == argumentList.size()) {
                    if (m.isStatic()) {
                        if (sameParameterTypes(m.getParameterTypeList(), argumentTypeList)) {
                            found = true;
                            returnType = m.getReturnType();
                            break;
                        } else {
                            throw new SemanticException(methodToken.getLexeme(), methodToken.getLineNumber(), "No se puede hacer una llamada estatica al metodo " + methodToken.getLexeme() + " porque no coinciden los tipos de los parametros.");
                        }
                    } else
                        throw new SemanticException(methodToken.getLexeme(), methodToken.getLineNumber(), "No se puede hacer una llamada estatica al metodo " + methodToken.getLexeme() + " porque no es estatico.");
                } else
                    throw new SemanticException(methodToken.getLexeme(), methodToken.getLineNumber(), "Diferente cantidad de parametros para el metodo " + methodToken.getLexeme());
            }
        }
        if (!found)
            throw new SemanticException(methodToken.getLexeme(), methodToken.getLineNumber(), "No se encontro el metodo estatico llamado " + methodToken.getLexeme() + " de " + operandToken.getLexeme());
    }

    public void generateCode() throws CodeGenerationException {
        System.out.println("generateCode:NodeAccessStaticMethod:" + operandToken.getLexeme()+":"+methodToken.getLexeme());
        for (NodeCompoundExpression n: argumentList)
            n.generateCode();
        System.out.println("generateCode:NodeAccessStaticMethod:mid");
        CodeGenerator.getInstance().addLine("PUSH "+CodeGenerator.generateLabelForMethod(searchStaticMethod())+" ; Apliamos el metodo");
        CodeGenerator.getInstance().addLine("CALL ; Llama al metodo en el tope de la pila");
        System.out.println("generateCode:NodeAccessStaticMethod:end");
    }

    protected MethodST searchStaticMethod() {
        MethodST methodToReturn = null;
        ClassST receiverClass = SymbolTable.getInstance().getClassWithName(operandToken.getLexeme());
        for (MethodST m : receiverClass.getMethods()) {
            if (methodToken.getLexeme().equals(m.getName())) {
                if (m.getParameterTypeList().size() == argumentList.size()) {
                    if (m.isStatic()) {
                        if (sameParameterTypes(m.getParameterTypeList(), argumentTypeList)) {
                            methodToReturn = m;
                            break;
                        }
                    }
                }
            }
        }
        return methodToReturn;
    }
}