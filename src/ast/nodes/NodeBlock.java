package ast.nodes;

import java.util.ArrayList;
import java.util.List;

public class NodeBlock extends NodeSentence implements Node {
    protected List<NodeSentence> nodeSentenceList;
    public NodeBlock(){
        nodeSentenceList = new ArrayList<NodeSentence>();
    }

    public void addSentence(NodeSentence nodeSentence) {
        nodeSentenceList.add(nodeSentence);
    }

    public void check() {

    }
    public boolean isAssignable() {
        return false;
    }
}
