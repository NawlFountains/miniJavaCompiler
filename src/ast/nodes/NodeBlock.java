package ast.nodes;

import lexical.SemanticException;
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
        if (rootOfMethod)
            System.out.println("ROOTBLOCK IS :"+this);
        System.out.println("NodeBlock:created:"+this);
    }
    public void insertLocalVariable(NodeVariableLocal nodeVariableLocal) {
        System.out.println("NodeBlock:insertLocalVariable:"+nodeVariableLocal.getStructure()+":");
        localVariables.add(nodeVariableLocal);
    }
    public boolean existsVariableWithName(String name) {
        boolean found = false;
        int i = 0;
        System.out.println("NodeBlock:existsVariableWithName:"+name);
        while (!found && i < localVariables.size()) {
            System.out.println("NodeBlock:existsVariableWithName:"+name+":"+localVariables.get(i));
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
            System.out.println(this+":addSentence:"+nodeSentence+"\n");
            nodeSentenceList.add(nodeSentence);
        }
    }

    public void check() throws SemanticException {
        System.out.println("About to check sentences in block");
        for (NodeSentence n : nodeSentenceList) {
            System.out.println("NodeBlock:check():Sentence:"+n);
            n.check();
        }
        System.out.println("NodeBlock:check():finish");
    }
    public boolean isAssignable() {
        return false;
    }
    public void setRoutineEnvironment(RoutineST routineEnvironment) {
        System.out.println(this+":NodeBlock:setRoutineEnviroment:"+routineEnvironment);
        this.routineEnvironment = routineEnvironment;
        for (NodeSentence ns: nodeSentenceList) {
            if (ns != null) {
                System.out.println("Propagated setRoutineEnviroment to "+ns+" with "+routineEnvironment);
                ns.setRoutineEnvironment(routineEnvironment);
            }
        }
    }
    public String getStructure() {
        System.out.println("NodeBlock:getStructure:Start");
        String toReturn = "";
        System.out.println("Sentence list "+nodeSentenceList.size());
        for (NodeSentence n : nodeSentenceList) {
            if (n != null) {
                System.out.println("NodeBlock:getStructure:Iteration:"+n);
                toReturn += n.getStructure()+"\n";
            }
        }
        return toReturn;
    }
}
