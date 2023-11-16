package ast.nodes.access;

import ast.nodes.*;
import lexical.SemanticException;
import lexical.Token;
import semantic.Type;

import java.util.ArrayList;
import java.util.List;

public abstract class NodeAccess extends NodeOperand implements Node {
    protected List<NodeCompoundExpression> argumentList;
    protected List<Type> argumentTypeList;
    protected NodeChained chainedNode;
    protected boolean isAttribute = true;
    public NodeAccess(Token callerToken) {
        super(callerToken);
        argumentList = new ArrayList<>();
        argumentTypeList = new ArrayList<>();
    }
    public void isNotAttribute() {
        isAttribute = false;
    }

    public void addChainingNode(NodeChained nodeChained){
        this.chainedNode = nodeChained;
    }
    public Token getToken(){
        return operandToken;
    }
    public void addArgument(NodeCompoundExpression nodeArgument) {
        argumentList.add(nodeArgument);
    }
    @Override
    public void check() throws SemanticException {
        
    }

    @Override
    public boolean isAssignable() {
        boolean assignable = false;
        if (chainedNode != null)
            assignable = chainedNode.isAssignable();
        return assignable;
    }
    public String getStructure(){
        String toReturn = "Access : "+operandToken.getLexeme()+"(";
        for (NodeCompoundExpression n : argumentList) {
            toReturn += n.getStructure();
        }
        toReturn += ")";
        if (chainedNode != null)
            toReturn += "."+chainedNode.getStructure();
        return toReturn;
    }
    boolean sameParameterTypes(List<Type> listOne, List<Type> listTwo) {
        boolean same = true;
        int i = 0;
        if(listOne.size() == listTwo.size()) {
            while (same && i < listOne.size()) {
                if (!listOne.get(i).equals(listTwo.get(i))) {
                    same = false;
                }
                i++;
            }
        } else {
            same = false;
        }
        return same;
    }
    protected void checkArgumentsList() throws SemanticException {
        argumentTypeList = new ArrayList<>();
        for (NodeCompoundExpression n : argumentList) {
            n.addParentBlock(this.getParentBlock());
            n.check();
            argumentTypeList.add(n.getReturnType());
        }
    }
}
