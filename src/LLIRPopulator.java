import llir.*;
import symbols.FunctionDescriptor;

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
        if (this.llirStack.empty()) return new LLIRNull();
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
    }

    public void setAssignmentVariable(LLIRExpression variable){
        if (this.peek() instanceof LLIRAssignment){
            LLIRAssignment assignment = (LLIRAssignment) this.peek();
            if(this.peek() instanceof  LLIRVariable)
                assignment.setVariable((LLIRVariable) variable);
            else if(this.peek() instanceof  LLIRArrayAccess){
                assignment.setVariable((LLIRArrayAccess) variable);
            }
        }

    }

    public void setAssignmentVariable(LLIRVariable variable){
        if (this.peek() instanceof LLIRAssignment){
            LLIRAssignment assignment = (LLIRAssignment) this.peek();
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

    public void addConditional(LLIRConditional conditional){
        if(peek() instanceof LLIRExpression){
            conditional.setLeftExpression((LLIRExpression) this.llirStack.pop());
        }

        this.llirStack.push(conditional);
    }

    public void addVariable(LLIRVariable variable){
        if(peek() instanceof LLIRArrayAccess ){
            LLIRArrayAccess arrayAccess = (LLIRArrayAccess) peek();
            arrayAccess.setArray(variable);
        }

    }

    public void addReturn(LLIRReturn returnLLIR){
        this.llirStack.push(returnLLIR);
    }

    public void popNegation(){

        if(peek() instanceof LLIRExpression){
            LLIRNode node = llirStack.pop();

            if(peek() instanceof LLIRNegation){
                ((LLIRNegation)peek()).setExpression((LLIRExpression) node);
            }else{
                llirStack.push(node);
            }
        }
    }
    public void popExpression(){

        //In case of a simple Assignment
        if(peek() instanceof LLIRExpression){
            LLIRNode node = llirStack.pop();

            if(peek() instanceof LLIRArithmetic){
                ((LLIRArithmetic)peek()).setRightExpression((LLIRExpression) node);
            }
            if(peek() instanceof LLIRConditional){
                ((LLIRConditional)peek()).setRightExpression((LLIRExpression)node);
            }
            if(peek() instanceof LLIRAssignment){
                ((LLIRAssignment)peek()).setExpression((LLIRExpression) node);
            }
            if(peek() instanceof LLIRNegation){
                ((LLIRNegation)peek()).setExpression((LLIRExpression) node);
            }
        }

        //In case of a complex assignment
        //Isto estava aqui um
        while (peek() instanceof LLIRArithmetic || peek() instanceof LLIRConditional || peek() instanceof LLIRNegation){
            LLIRExpression actual = (LLIRExpression) this.llirStack.pop();

            if (peek() instanceof LLIRArithmetic){
                LLIRArithmetic previous = (LLIRArithmetic) peek();
                previous.setRightExpression(actual);
            }
            else if (peek() instanceof LLIRConditional){
                LLIRConditional previous = (LLIRConditional) peek();
                previous.setRightExpression(actual);
            }

            /*else if (peek() instanceof LLIRIfElseBlock){
                LLIRIfElseBlock previous = (LLIRIfElseBlock) peek();
                previous.setExpression(actual);
                break;
            }*/
            else if (peek() instanceof LLIRNegation){
                LLIRNegation previous = (LLIRNegation) peek();
                previous.setExpression(actual);
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

    public void popReturn(){
        if(lastIsLLIRExpression()){
            LLIRExpression expression = (LLIRExpression) this.llirStack.pop();
            if(peek() instanceof  LLIRReturn){
                LLIRReturn returnLLIR = (LLIRReturn) peek();
                returnLLIR.setExpression(expression);
            }
        }
    }
    public void popArrayInstantiation(){
        if(lastIsLLIRExpression()){
            LLIRExpression expression = (LLIRExpression) this.llirStack.pop();
            if(peek() instanceof  LLIRArrayInstantiation){
                LLIRArrayInstantiation arrayInst = (LLIRArrayInstantiation) peek();
                arrayInst.setSize(expression);
            }
        }
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

    public void addStatement(FunctionDescriptor currentFunctionDescriptor){
        if(!llirStack.empty()) {
            LLIRNode node = this.llirStack.pop();
            if (peek() instanceof LLIRIfElseBlock) {
                LLIRIfElseBlock ifElseBlock = (LLIRIfElseBlock) peek();
                if (!ifElseBlock.isFinishedElse()) {
                    ifElseBlock.addNode(node);
                    return;
                }
            } else if (peek() instanceof LLIRWhileBlock) {
                LLIRWhileBlock whileBlock = (LLIRWhileBlock) peek();
                if (!whileBlock.isFinished()) {
                    whileBlock.addNode(node);
                    return;
                }
            }
            currentFunctionDescriptor.addLLIRNode(node);
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

    public void popBlock(){
        if(peek() instanceof LLIRIfElseBlock) {
            LLIRIfElseBlock node = (LLIRIfElseBlock) this.llirStack.peek();
            node.setFinishedElse(true);
        } else if(peek() instanceof LLIRWhileBlock) {
            LLIRWhileBlock node = (LLIRWhileBlock) this.llirStack.peek();
            node.setFinished(true);
        }
    }

    public void popBlockExpression(){
        if(peek() instanceof LLIRExpression) {
            LLIRExpression node = (LLIRExpression) this.llirStack.pop();
            if (peek() instanceof LLIRIfElseBlock) {
                LLIRIfElseBlock ifElseBlock = (LLIRIfElseBlock) peek();
                ifElseBlock.setExpression(node);
            }
            if (peek() instanceof LLIRWhileBlock) {
                LLIRWhileBlock whileBlock = (LLIRWhileBlock) peek();
                whileBlock.setExpression(node);
            }
        }
    }

    public boolean lastIsArithmetic(){
        if (this.peek() instanceof LLIRArithmetic) return true;
        return false;
    }

    public boolean lastIsConditional(){
        if(this.peek() instanceof LLIRConditional) return true;
        return false;
    }

    public boolean lastIsAssignment(){
        if (this.peek() instanceof LLIRAssignment) return true;
        return false;
    }
    public boolean lastIsLLIRExpression(){
        if (this.peek() instanceof LLIRExpression) return true;
        return false;
    }
    public boolean shouldAddArithmetic(){
        if (lastIsArithmetic()){
            LLIRArithmetic arithmetic = (LLIRArithmetic) peek();
            if (arithmetic.foundOperator()) return true;
        }
        return false;
    }
    public boolean shouldAddConditional(){
        if (lastIsConditional()){
            LLIRConditional conditional = (LLIRConditional) peek();
            if (conditional.foundOperator()) return true;
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
        }else if(lastIsConditional()){
            LLIRConditional conditional = (LLIRConditional) peek();
            switch(nodeId){
                case JavammTreeConstants.JJTAND:{
                    conditional.setOperation(ConditionalOperation.AND);
                    break;
                }
                case JavammTreeConstants.JJTLESS:{
                    conditional.setOperation(ConditionalOperation.LESS_THAN);
                    break;
                }
                case JavammTreeConstants.JJTNEGATION:{
                    conditional.setOperation(ConditionalOperation.NEGATION);
                    break;
                }
            }
        }
    }




    public String getBlockStatements(LLIRIfElseBlock ifElseBlock,String identation){
        String stack = "";
        stack = identation +  "IF:\n";
        for (LLIRNode node : ifElseBlock.getIfNodes()){
            stack += identation + getNodeString(node,identation + "  ");
        }
        stack +=identation + identation + "ELSE:\n";
        for (LLIRNode node : ifElseBlock.getElseNodes()){
            stack += identation + getNodeString(node,identation + "  ");
        }
        return stack;
    }

    public String getBlockStatements(LLIRWhileBlock whileBlock,String identation){
        String stack = "";
        for (LLIRNode node : whileBlock.getNodes()){
            stack += identation + getNodeString(node,identation + "  ");
        }
        return stack;
    }

    public void printStack(){

        String stack = "";
        Stack<LLIRNode> copy = (Stack<LLIRNode>) this.llirStack.clone();
        while (!copy.empty()){
            LLIRNode node  =copy.pop();
            stack += getNodeString(node,"");
        }
        System.out.println("STACK\n\n" + stack);

    }

    private String getNodeString(LLIRNode node,String identation){

        if (node instanceof LLIRAssignment){
            return identation +"Assignment\n";
        }else if(node instanceof LLIRArithmetic){
            return identation +"Arithmetic\n";
        }else if(node instanceof LLIRConditional) {
            return identation +"Conditional\n";
        }else if(node instanceof LLIRIfElseBlock) {
            String s = identation + "IFElseBlock\n";
            s += identation + getBlockStatements((LLIRIfElseBlock) node,identation);
            return s;
        }else if(node instanceof LLIRWhileBlock) {
            String s = identation + "While\n";
            s += identation + getBlockStatements((LLIRWhileBlock) node,identation);
            return s;
        }else if(node instanceof LLIRBoolean) {
            return identation + "Boolean\n";
        }else if(node instanceof LLIRInteger) {
            return identation + "Integer\n";
        }else if(node instanceof LLIRArrayInstantiation){
            return identation +"Array Instantiation\n";
        }else if(node instanceof LLIRArrayAccess){
            return identation +"Array Access\n";
        }else if (node instanceof LLIRMethodCall) {
            return identation +"Method Call\n";
        }else if(node instanceof LLIRImport){
            return identation +"Import\n";
        }else if(node instanceof LLIRNegation){
            return identation +"Negation\n";
        }else if(node instanceof LLIRExpression){
            return identation +"Expression\n";
        }else if(node instanceof LLIRReturn){
            return identation +"Return\n";
        }
        return "NODE\n";
    }






}
