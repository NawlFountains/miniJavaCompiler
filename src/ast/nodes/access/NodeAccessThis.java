package ast.nodes.access;

import ast.nodes.Node;
import ast.nodes.NodeCompoundExpression;
import ast.nodes.access.NodeAccess;
import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
import lexical.SemanticException;
import lexical.Token;
import semantic.ReferenceType;
import semantic.SymbolTable;
import semantic.entities.AttributeST;
import semantic.entities.RoutineST;

public class NodeAccessThis extends NodeAccess implements Node {
    public NodeAccessThis(Token operandToken) {
        super(operandToken);
    }

    @Override
    public void check() throws SemanticException {
        returnType = new ReferenceType(getRootBlock().getRoutineEnvironment().getOwnerClass().getClassName());
        standaloneReturnType = returnType;
        if (chainedNode != null) {
            chainedNode.check();
            returnType = chainedNode.getReturnType();
        }
    }

    public String getStructure() {
        String toReturn = "this";
        if (argumentList.size() > 0) {
            toReturn+="(";
            for (NodeCompoundExpression n : argumentList) {
                toReturn += n.getStructure();
            }
            toReturn +=")";
        }
        if (chainedNode != null) {
            toReturn += "."+chainedNode.getStructure();
        }
        return toReturn;
    }
    @Override
    public void generateCodeForAssignment() throws CodeGenerationException {
        //We asume only assignment will ask this
        CodeGenerator.getInstance().addLine("LOAD 3");
        if (chainedNode != null)
            chainedNode.generateCodeForAssignment();
    }
}
