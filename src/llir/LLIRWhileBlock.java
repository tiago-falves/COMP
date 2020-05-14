package llir;

import java.util.ArrayList;
import java.util.List;

public class LLIRWhileBlock extends LLIRNode {
    private LLIRExpression whileExpression;
    private List<LLIRNode> whileNodes;

    public LLIRWhileBlock(){
        whileExpression = null;
        whileNodes = new ArrayList<>();
    }

    public void addNode(LLIRNode node){
        whileNodes.add(node);
    }

    public void setExpression(LLIRExpression expression){
        whileExpression = expression;
    }

    public List<LLIRNode> getNodes(){
        return whileNodes;
    }

    public LLIRExpression getExpression(){
        return whileExpression;
    }
}