package llir;

import java.util.ArrayList;
import java.util.List;

public class LLIRIfElseBlock extends LLIRNode{
    private LLIRExpression ifExpression;
    private List<LLIRNode> ifNodes;
    private List<LLIRNode> elseNodes;
    private boolean foundElse;
    private boolean finishedElse;


    public LLIRIfElseBlock(){
        ifExpression = null;
        ifNodes = new ArrayList<>();
        elseNodes = new ArrayList<>();
        this.foundElse = false;
        this.finishedElse = false;

    }

    public void setExpression(LLIRExpression expression){
        ifExpression = expression;
    }

    public void addNode(LLIRNode node){
        if(!this.foundElse){
            this.addIfNode(node);
        }
        else {
            this.addElseNode(node);
        }
    }

    public void addIfNode(LLIRNode node){
        ifNodes.add(node);
    }

    public void addElseNode(LLIRNode node){
        elseNodes.add(node);
    }

    public LLIRExpression getExpression(){
        return ifExpression;
    }

    public List<LLIRNode> getIfNodes(){
        return ifNodes;
    }

    public List<LLIRNode> getElseNodes(){
        return elseNodes;
    }

    public boolean isFinishedElse() {
        return finishedElse;
    }

    public void setFinishedElse(boolean finishedElse) {
        this.finishedElse = finishedElse;
    }

    public boolean isFoundElse() {
        return foundElse;
    }

    public void setFoundElse(boolean foundElse) {
        this.foundElse = foundElse;
    }
}