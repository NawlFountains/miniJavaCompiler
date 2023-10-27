package ast.nodes.access;

import ast.nodes.Node;
import lexical.SemanticException;
import lexical.Token;
import semantic.SymbolTable;
import semantic.entities.ClassST;
import semantic.entities.MethodST;

public class NodeAccessStaticMethod extends NodeAccess implements Node {
    protected Token methodToken;
    public NodeAccessStaticMethod(Token callerToken, Token methodToken) {
        super(callerToken);
        this.methodToken = methodToken;
    }

    @Override
    public void check() throws SemanticException {
        System.out.println("NodeAccessStaticMethod:check()");
        ClassST receiverClass = SymbolTable.getInstance().getClassWithName(operandToken.getLexeme());
        if (receiverClass == null) {
            throw new SemanticException(operandToken.getLexeme(),operandToken.getLineNumber(),"No existe la clase "+operandToken.getLexeme()+" referida.");
        }
        for (MethodST m : receiverClass.getMethods()) {
            if (operandToken.getLexeme().equals(m.getName())) {
                if (m.getParameterTypeList().size() == argumentList.size()) {
                    if (m.isStatic()) {
                        if (sameParameterTypes(m.getParameterTypeList(),argumentTypeList)) {
                            break;
                        }
                        else
                            throw new SemanticException(methodToken.getLexeme(),methodToken.getLineNumber(),"No se puede hacer una llamada estatica al metodo "+methodToken.getLexeme()+" porque no coinciden los tipos de los parametros.");
                    } else
                        throw new SemanticException(methodToken.getLexeme(),methodToken.getLineNumber(),"No se puede hacer una llamada estatica al metodo "+methodToken.getLexeme()+" porque no es estatico.");

                } else
                    throw new SemanticException(methodToken.getLexeme(),methodToken.getLineNumber(),"Diferente cantidad de parametros para el metodo "+methodToken.getLexeme());
            }
        }
    }
}
