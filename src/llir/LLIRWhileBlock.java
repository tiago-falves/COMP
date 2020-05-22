package llir;

import java.util.ArrayList;
import java.util.List;

public class LLIRWhileBlock extends LLIRNode {
    private LLIRExpression whileExpression;
    private List<LLIRNode> whileNodes;
    boolean finished;

    public LLIRWhileBlock(){
        whileExpression = null;
        whileNodes = new ArrayList<>();
        this.finished = false;
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

    public boolean isFinished() {
        return finished;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}