package ast.nodes;

import lexical.SemanticException;
import semantic.Type;
import semantic.entities.RoutineST;

import java.util.ArrayList;
import java.util.List;

public class NodeBlock extends NodeSentence implements Node {
    protected List<NodeSentence> nodeSentenceList;
    protected List<NodeVariableLocal> localVariables;
    protected boolean rootOfMethod;
    public NodeBlock(boolean isRoot){
        nodeSentenceList = new ArrayList<>();
        localVariables = new ArrayList<>();
        rootOfMethod = isRoot;
    }
    public void insertLocalVariable(NodeVariableLocal nodeVariableLocal) {
        localVariables.add(nodeVariableLocal);
    }
    public Type getVariableType(String name) {
        Type toReturn = null;
        for (NodeVariableLocal v : localVariables) {
            if (v.variableIdToken.getLexeme().equals(name)) {
                toReturn = v.variableType;
            }
        }
        return toReturn;
    }
    public boolean existsVariableWithName(String name) {
        boolean found = false;
        int i = 0;
        while (!found && i < localVariables.size()) {
            if (localVariables.get(i).variableIdToken.getLexeme().equals(name))
                found = true;
            else
                i++;
        }
        return found;
    }
    public boolean isRoot() {
        return rootOfMethod;
    }

    public void addSentence(NodeSentence nodeSentence) {
        if (nodeSentence != null) {
            nodeSentenceList.add(nodeSentence);
        }
    }

    public void check() throws SemanticException {
        for (NodeSentence n : nodeSentenceList) {
            n.check();
        }
    }
    public boolean isAssignable() {
        return false;
    }
    public void setRoutineEnvironment(RoutineST routineEnvironment) {
        this.routineEnvironment = routineEnvironment;
        for (NodeSentence ns: nodeSentenceList) {
            if (ns != null) {
                ns.setRoutineEnvironment(routineEnvironment);
            }
        }
    }
    public String getStructure() {
        String toReturn = "";
        for (NodeSentence n : nodeSentenceList) {
            if (n != null) {
                toReturn += n.getStructure()+"\n";
            }
        }
        return toReturn;
    }
}
