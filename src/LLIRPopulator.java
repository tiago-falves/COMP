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

    public void addImport(LLIRImport llirImport){
        if(peek() instanceof LLIRMethodCall){
            LLIRMethodCall methodCall = (LLIRMethodCall) llirStack.pop();
            llirImport.setParametersExpressions(methodCall.getParametersExpressions());
            this.llirStack.add(llirImport);
        }
    }

    public void popMethodCall(){


    }

    public void popParenthesis(){
        if (this.llirStack.empty()) return;
        LLIRExpression node = (LLIRExpression) this.llirStack.pop();
        if(peek() instanceof LLIRParenthesis){
            ((LLIRParenthesis) peek()).setExpression(node);
        }

    }

    public boolean lastIsFunctionCall(){
        if (llirStack.isEmpty()) return false;
        if(peek() instanceof LLIRMethodCall) return true;
        return false;
    }


    public void addArithmetic(LLIRArithmetic arithmetic){

        if(peek() instanceof LLIRExpression){
            arithmetic.setLeftExpression((LLIRExpression) this.llirStack.pop());
        }
        this.llirStack.push(arithmetic);
    }

    public void addReturn(LLIRReturn returnLLIR){
        this.llirStack.push(returnLLIR);
    }

    public void popArithmetics(){

        //In case of a simple Assignment
        if(peek() instanceof LLIRExpression){
            LLIRNode node = llirStack.pop();

            if(peek() instanceof LLIRArithmetic){
                ((LLIRArithmetic)peek()).setRightExpression((LLIRExpression) node);
            }
            if(peek() instanceof LLIRAssignment){
                ((LLIRAssignment)peek()).setExpression((LLIRExpression) node);
            }
        }

        //In case of a complex assignment
        while (peek() instanceof LLIRArithmetic || peek() instanceof LLIRExpression ){
            LLIRExpression actual = (LLIRExpression) this.llirStack.pop();

            if (peek() instanceof LLIRArithmetic){
                LLIRArithmetic previous = (LLIRArithmetic) peek();
                previous.setRightExpression(actual);
            }
            else if (peek() instanceof LLIRAssignment){
                LLIRAssignment previous = (LLIRAssignment) peek();
                previous.setExpression(actual);
                break;
            }
            else {
                this.llirStack.push(actual);
                break;
            }
        }
    }

    public LLIRNode peek(){
        if(!this.llirStack.empty()){
            return this.llirStack.peek();
        }
        return new LLIRNull();


    }

    public void popArguments(){

        List<LLIRExpression> arguments = new ArrayList<>();
        while (peek() instanceof LLIRExpression && !lastIsFunctionCall()){
            LLIRExpression actual = (LLIRExpression) this.llirStack.pop();
            arguments.add(0,actual);
        }

        if (peek() instanceof LLIRMethodCall){
            LLIRMethodCall function = (LLIRMethodCall) peek();
            function.setParametersExpressions(arguments);
        }

    }

    //If the stack has an expression before assignment, set assignment expression
    public void popBeforeAssignment(){
        if(peek() instanceof LLIRExpression) {
            LLIRExpression node = (LLIRExpression) this.llirStack.pop();
            if (peek() instanceof LLIRAssignment) {
                LLIRAssignment assignment = (LLIRAssignment) peek();
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
            LLIRArithmetic arithmetic = (LLIRArithmetic) peek();
            if (arithmetic.foundOperator()) return true;
        }
        return false;
    }

    public void addOperator(int nodeId){
        if(lastIsArithmetic()) {
            LLIRArithmetic arithmetic = (LLIRArithmetic) peek();
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

    public void addClassInstantiation(LLIRClassVariableInstantiation variable){
        if (peek() instanceof LLIRAssignment){
            LLIRAssignment assignment = (LLIRAssignment) peek();
            assignment.setExpression(variable);
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
            }else if(node instanceof LLIRImport){
                stack += "Import\n";
            }else if(node instanceof LLIRExpression){
                stack += "Expression\n";
            }else if(node instanceof LLIRReturn){
                stack += "Return\n";
            }
        }

        System.out.println("STACK\n\n" + stack);

    }






}
