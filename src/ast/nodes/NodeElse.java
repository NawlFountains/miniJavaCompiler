package ast.nodes;

public class NodeElse extends NodeSentence implements Node{
    protected NodeIf nodeIf;
    protected NodeSentence elseSentence;

    public NodeElse(NodeIf nodeIf, NodeSentence elseSentence) {
        this.nodeIf = nodeIf;
        this.elseSentence = elseSentence;
    }
    @Override
    public void check() {

    }

    @Override
    public boolean isAssignable() {
        return false;
    }
    public String getStructure(){
        return elseSentence.getStructure()+"\n";
    }
}
