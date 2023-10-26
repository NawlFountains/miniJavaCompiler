package ast.nodes;

import java.util.ArrayList;
import java.util.List;

public class NodeBlock extends NodeSentence implements Node {
    protected List<NodeSentence> nodeSentenceList;
    public NodeBlock(){
        nodeSentenceList = new ArrayList<>();
    }

    public void addSentence(NodeSentence nodeSentence) {
        if (nodeSentence != null) {
            System.out.println("NodeBlock:addSentence:"+nodeSentence+"\n");
            nodeSentenceList.add(nodeSentence);
        }
    }

    public void check() {

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
