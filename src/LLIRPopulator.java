import llir.*;

import java.util.Stack;

public class LLIRPopulator {



    private Stack<LLIRNode> llirStack;

    public LLIRPopulator() {
        this.llirStack = new Stack<>();
    }

    public void addLLIR(LLIRNode node){
        this.llirStack.push(node);
    }

    public LLIRNode popLLIR(){
        return this.llirStack.pop();
    }


    public void addAssignment(LLIRAssignment assignment){
        this.llirStack.push(assignment);
    }

    public Stack<LLIRNode> getLlirStack() {
        return llirStack;
    }

    public void setLlirStack(Stack<LLIRNode> llirStack) {
        this.llirStack = llirStack;
    }

    public void addInteger(LLIRInteger integer){
        addExpression(integer);
    }

    //Simple Expression
    public void addExpression(LLIRExpression expression){
        if (llirStack.peek() instanceof LLIRAssignment){
            LLIRAssignment assignment = (LLIRAssignment) llirStack.peek();
            assignment.setExpression(expression);
        }
        if (llirStack.peek() instanceof LLIRArithmetic){
            LLIRArithmetic assignment = (LLIRArithmetic) llirStack.peek();
            assignment.setExpression(expression);
        }
    }

    public void setAssignmentVariable(LLIRVariable variable){
        if (llirStack.peek() instanceof LLIRAssignment){
            LLIRAssignment assignment = (LLIRAssignment) llirStack.peek();
            assignment.setVariable(variable);
        }

    }
    public void addMethodCall(LLIRMethodCall methodCall){
        //If empty then simpleFunctionCall
        if (this.llirStack.empty()){
            this.llirStack.add(methodCall);
        }
    }

    public void addArithmetic(LLIRArithmetic arithmetic){
        this.llirStack.push(arithmetic);
    }





}
