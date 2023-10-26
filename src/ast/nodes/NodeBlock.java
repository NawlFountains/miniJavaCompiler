package ast.nodes;

import lexical.SemanticException;
import semantic.entities.RoutineST;

import java.util.ArrayList;
import java.util.List;

public class NodeBlock extends NodeSentence implements Node {
    protected List<NodeSentence> nodeSentenceList;
    protected RoutineST routineEnvironment;
    public NodeBlock(){
        nodeSentenceList = new ArrayList<>();
    }

    public void addSentence(NodeSentence nodeSentence) {
        if (nodeSentence != null) {
            System.out.println("NodeBlock:addSentence:"+nodeSentence+"\n");
            nodeSentenceList.add(nodeSentence);
        }
    }
    public void setRoutineEnvironment(RoutineST routineEnvironment) {
        this.routineEnvironment = routineEnvironment;
    }
    public RoutineST getRoutineEnvironment() {
        System.out.println("NodeBlock:getRoutineEnviroment:"+routineEnvironment);
        return routineEnvironment;
    }

    public void check() throws SemanticException {
        System.out.println("About to check sentences in block");
        for (NodeSentence n : nodeSentenceList) {
            System.out.println("NodeBlock:check():Sentence:"+n);
            n.check();
        }
    }
    public boolean isAssignable() {
        return false;
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
