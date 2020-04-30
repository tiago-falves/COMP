import llir.*;

import java.util.ArrayList;
import java.util.List;
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

        this.llirStack.push(expression);

        /*if (llirStack.peek() instanceof LLIRAssignment){

            this.llirStack.push(expression);
            //LLIRAssignment assignment = (LLIRAssignment) llirStack.peek();
            //assignment.setExpression(expression);
        }
        if (llirStack.peek() instanceof LLIRArithmetic){
            LLIRArithmetic arithmetic = (LLIRArithmetic) llirStack.peek();
            arithmetic.setExpression(expression);
        }*/
    }

    public void setAssignmentVariable(LLIRVariable variable){
        if (llirStack.peek() instanceof LLIRAssignment){
            LLIRAssignment assignment = (LLIRAssignment) llirStack.peek();
            assignment.setVariable(variable);
        }

    }
    public void addMethodCall(LLIRMethodCall methodCall){
        //If empty then simpleFunctionCall
        addExpression(methodCall);
    }

    public void popMethodCall(){

    }

    public boolean lastIsFunctionCall(){
        if (llirStack.isEmpty()) return false;
        if(this.llirStack.peek() instanceof LLIRMethodCall) return true;
        return false;
    }

    public void addArithmetic(LLIRArithmetic arithmetic){

        if(this.llirStack.peek() instanceof LLIRExpression){
            arithmetic.setLeftExpression((LLIRExpression) this.llirStack.pop());
        }
        this.llirStack.push(arithmetic);
    }

    public void addReturn(LLIRReturn returnLLIR){
        this.llirStack.push(returnLLIR);
    }

    public void popArithmetics(){

        //In case of a simple Assignment
        if(this.llirStack.peek() instanceof LLIRExpression){
            LLIRNode node = llirStack.pop();

            if(this.llirStack.peek() instanceof LLIRArithmetic){
                ((LLIRArithmetic)this.llirStack.peek()).setRightExpression((LLIRExpression) node);
            }
            if(this.llirStack.peek() instanceof LLIRAssignment){
                ((LLIRAssignment)this.llirStack.peek()).setExpression((LLIRExpression) node);
            }
        }

        //In case of a complex assignment
        while (this.llirStack.peek() instanceof LLIRArithmetic){
            LLIRArithmetic actual = (LLIRArithmetic) this.llirStack.pop();

            if (this.llirStack.peek() instanceof LLIRArithmetic){
                LLIRArithmetic previous = (LLIRArithmetic) this.llirStack.peek();
                previous.setRightExpression(actual);
            }
            else if (this.llirStack.peek() instanceof LLIRAssignment){
                LLIRAssignment previous = (LLIRAssignment) this.llirStack.peek();
                previous.setExpression(actual);
                break;
            }
            else if (this.llirStack.peek() instanceof LLIRMethodCall){
                this.llirStack.push(actual);
                break;
            }
        }
    }

    public void popArguments(){

        List<LLIRExpression> arguments = new ArrayList<>();
        while (this.llirStack.peek() instanceof LLIRExpression && !lastIsFunctionCall()){
            LLIRExpression actual = (LLIRExpression) this.llirStack.pop();
            arguments.add(actual);
        }

        if (this.llirStack.peek() instanceof LLIRMethodCall){
            LLIRMethodCall function = (LLIRMethodCall) this.llirStack.peek();
            function.setParametersExpressions(arguments);
        }

    }

    //If the stack has an expression before assignment, set assignment expression
    public void popBeforeAssignment(){
        if(this.llirStack.peek() instanceof LLIRExpression) {
            LLIRExpression node = (LLIRExpression) this.llirStack.pop();
            if (this.llirStack.peek() instanceof LLIRAssignment) {
                LLIRAssignment assignment = (LLIRAssignment) this.llirStack.peek();
                assignment.setExpression(node);
            }
        }
    }

    public boolean lastIsArithmetic(){
        if (llirStack.peek() instanceof LLIRArithmetic) return true;
        return false;
    }

    public boolean lastIsAssignment(){
        if (llirStack.peek() instanceof LLIRAssignment) return true;
        return false;
    }
    public boolean lastIsLLIRExpression(){
        if (llirStack.peek() instanceof LLIRExpression) return true;
        return false;
    }
    public boolean shouldAddArithmetic(){
        if (lastIsArithmetic()){
            LLIRArithmetic arithmetic = (LLIRArithmetic) this.llirStack.peek();
            if (arithmetic.foundOperator()) return true;
        }
        return false;
    }

    public void addOperator(int nodeId){
        if(lastIsArithmetic()) {
            LLIRArithmetic arithmetic = (LLIRArithmetic) this.llirStack.peek();
            switch (nodeId) {
                case JavammTreeConstants.JJTMULT: {
                    arithmetic.setOperation(ArithmeticOperation.MULTIPLICATION);
                    break;
                }
                case JavammTreeConstants.JJTDIV: {
                    arithmetic.setOperation(ArithmeticOperation.DIVISION);

                    break;
                }
                case JavammTreeConstants.JJTPLUS: {
                    arithmetic.setOperation(ArithmeticOperation.SUM);
                    break;
                }
                case JavammTreeConstants.JJTMINUS: {
                    arithmetic.setOperation(ArithmeticOperation.SUBTRACTION);
                    break;
                }
            }
        }
    }

    public void printStack(){
        String stack = "";

        Stack<LLIRNode> copy = (Stack<LLIRNode>) this.llirStack.clone();
        while (!copy.empty()){
            LLIRNode node  =copy.pop();
            if (node instanceof LLIRAssignment){
                stack += "Assignment\n";
            }else if(node instanceof LLIRArithmetic){
                stack += "Arithmetic\n";
            }else if (node instanceof LLIRMethodCall) {
                stack += "Method Call\n";
            }else if(node instanceof LLIRExpression){
                stack += "Expression\n";

            }
        }

        System.out.println("STACK\n\n" + stack);

    }






}
