package ast.nodes;

import ast.nodes.access.NodeAccess;
import ast.nodes.access.NodeAccessMetVar;
import ast.nodes.access.NodeChained;
import filemanager.CodeGenerationException;
import filemanager.CodeGenerator;
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
    public int getLocalVariablePosition(String variableName) {
        int position = -1;
        for (int i = 0 ; i < localVariables.size(); i++) {
            if ( localVariables.get(i).getName().equals(variableName)) {
                position = i;
                break;
            }
        }
        return position;
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
    public int getAmountOfVariables() {
        return localVariables.size();
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

    @Override
    public void generateCode() throws CodeGenerationException {
        System.out.println("generateCode:NodeBlock:"+this);
        for (NodeSentence ns: nodeSentenceList) {
            if (ns != null) {
                System.out.println("generateCode:NodeBlock:Lopp:"+ns);
                ns.generateCode();
                // If the sentence starts with a call, that has a chained node, but its not an assignment then we could throw out the value
                if (ns instanceof NodeAccessMetVar && ((NodeAccessMetVar) ns).hasChainedNode() && !((NodeAccessMetVar) ns).getReturnType().toString().equals("void")) {
                    CodeGenerator.getInstance().addLine("POP");
                }
            }
        }
    }
}
