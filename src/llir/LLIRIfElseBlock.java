package llir;

import java.util.ArrayList;
import java.util.List;

public class LLIRIfElseBlock {
    public LLIRExpression ifExpression;
    public List<LLIRNode> ifNodes;
    public List<LLIRNode> elseNodes;

    public LLIRIfElseBlock(){
        ifExpression = null;
        ifNodes = new ArrayList<>();
        elseNodes = new ArrayList<>();
    }

    public void setExpression(LLIRExpression expression){
        ifExpression = expression;
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
}