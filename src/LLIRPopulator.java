import llir.*;
import symbols.FunctionDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LLIRPopulator {


    //Stack that deals with the created LLIR
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

    //Add a LLIR Expression to the stack
    public void addExpression(LLIRExpression expression){
        this.llirStack.push(expression);
    }

    //Sets the variable of the assignment
    public void setAssignmentVariable(LLIRExpression variable){
        if (this.peek() instanceof LLIRAssignment){
            LLIRAssignment assignment = (LLIRAssignment) this.peek();
            if(variable instanceof  LLIRVariable)
                assignment.setVariable((LLIRVariable) variable);
            else if(variable instanceof LLIRArrayAccess){
                assignment.setVariable((LLIRArrayAccess) variable);
            }
        }
    }

    //Sets the Array Acess in the correponding LLIT
    public void popArrayAcessExpression(){
        if(lastIsLLIRExpression()){
            LLIRExpression expression =(LLIRExpression) this.llirStack.pop();
            if(peek() instanceof  LLIRArrayAccess){
                LLIRArrayAccess access = (LLIRArrayAccess) this.llirStack.peek();
                access.setAccess(expression);
            }

        }
    }

    public void popArrayAfterFunction(){
        if(!lastIsFunctionCall()){
            LLIRExpression expression = (LLIRExpression) this.llirStack.pop();
            if(expression instanceof LLIRArrayAccess){
                LLIRArrayAccess access = (LLIRArrayAccess) expression;
                LLIRExpression functionCall = (LLIRExpression) this.llirStack.pop();
                access.setArray(functionCall);
                this.llirStack.push(access);
            }else if(expression instanceof LLIRArrayLength){
                LLIRArrayLength arrayLength = (LLIRArrayLength) expression;
                LLIRExpression functionCall = (LLIRExpression) this.llirStack.pop();
                arrayLength.setArray(functionCall);
                this.llirStack.push(arrayLength);
            }   
        }
    }

    public void setAssignmentVariable(LLIRVariable variable){
        if (this.peek() instanceof LLIRAssignment){
            LLIRAssignment assignment = (LLIRAssignment) this.peek();
            assignment.setVariable(variable);
        }

    }
    //Add Method call to the stack
    public void addMethodCall(LLIRMethodCall methodCall){
        addExpression(methodCall);
    }

    //Add import to the stack and sets its parameters
    public void addImport(LLIRImport llirImport){
        if(peek() instanceof LLIRMethodCall){
            LLIRMethodCall methodCall = (LLIRMethodCall) llirStack.pop();
            llirImport.setParametersExpressions(methodCall.getParametersExpressions());
            llirImport.setVariableName(methodCall.getClassName());
            this.llirStack.add(llirImport);
        }
    }

    //Pops Parenthsis of the stack and sets it's expression
    public void popParenthesis(){
        if (this.llirStack.empty()) return;
        LLIRExpression node = (LLIRExpression) this.llirStack.pop();
        if(peek() instanceof LLIRParenthesis){
            ((LLIRParenthesis) peek()).setExpression(node);
        }

    }

    //True if the top member of the stack is a Method
    public boolean lastIsFunctionCall(){
        if (llirStack.isEmpty()) return false;
        if(peek() instanceof LLIRMethodCall) return true;
        return false;
    }


    //Adds an arithmetic function
    public void addArithmetic(LLIRArithmetic arithmetic){
        if(peek() instanceof LLIRExpression){
            arithmetic.setLeftExpression((LLIRExpression) this.llirStack.pop());
        }
        this.llirStack.push(arithmetic);
    }

    //Adds a conditional Expressions and sets it's left expression
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

    //Pops the negation expression from the stack and sets the corresponding LLIR
    public void popNegation(){

        if(peek() instanceof LLIRExpression){
            LLIRNode node = llirStack.pop();

            if(peek() instanceof LLIRNegation){
                ((LLIRNegation)peek()).setExpression((LLIRExpression) node);
            }else llirStack.push(node);
        }
    }

    //Pops simple expressions from the stack and deals with all the logic
    public void popExpression(){

        //In case of a simple Assignment
        if(peek() instanceof LLIRExpression){
            LLIRNode node = llirStack.pop();

            if(peek() instanceof LLIRArithmetic){
                ((LLIRArithmetic)peek()).setRightExpression((LLIRExpression) node);
            }
            else if(peek() instanceof LLIRConditional){
                ((LLIRConditional)peek()).setRightExpression((LLIRExpression)node);
            }
            else if(peek() instanceof LLIRAssignment){
                ((LLIRAssignment)peek()).setExpression((LLIRExpression) node);
            }
            else if(peek() instanceof LLIRNegation){
                ((LLIRNegation)peek()).setExpression((LLIRExpression) node);
            }
            else if(peek() instanceof LLIRArrayInstantiation){
                ((LLIRArrayInstantiation)peek()).setSize((LLIRExpression)node);
            }
            else if(peek() instanceof LLIRReturn){
                ((LLIRReturn) peek()).setExpression((LLIRExpression) node);
            }
            else{
                this.llirStack.push(node);
            }
        }

        //In case of a complex assignment
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

    public void fixConditional(){
        if(peek() instanceof LLIRInteger){
            LLIRInteger iLlirInteger = (LLIRInteger) this.llirStack.pop();
            if(peek() instanceof LLIRArithmetic){
                LLIRArithmetic actual = (LLIRArithmetic) this.llirStack.pop();
                actual.setRightExpression(iLlirInteger);
                while (peek() instanceof LLIRArithmetic){
                    ((LLIRArithmetic) peek()).setRightExpression(actual);
                    actual = (LLIRArithmetic)this.llirStack.pop();
                }
                this.llirStack.push(actual);
            }else{
                this.llirStack.push(iLlirInteger);
            }
        }
    }

    //Peeks from the stack if it is not empty
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
            }else if(peek() instanceof LLIRAssignment){
                LLIRAssignment assignment = (LLIRAssignment) peek();
                assignment.setExpression(expression);
            }
        }
    }

    //Pops functions arguments and deals with all the logic
    public void popArguments(int parametersNumber){
        List<LLIRExpression> arguments = new ArrayList<>();
        
        if(parametersNumber == 0) {
            if (peek() instanceof LLIRMethodCall){
                LLIRMethodCall function = (LLIRMethodCall) peek();
                function.setParametersExpressions(arguments);
            }
            return;
        }

        while (peek() instanceof LLIRExpression && parametersNumber > 0){
            LLIRExpression actual = (LLIRExpression) this.llirStack.pop();
            arguments.add(0,actual);
            parametersNumber--;
        }
        if (peek() instanceof LLIRMethodCall){
            LLIRMethodCall function = (LLIRMethodCall) peek();
            function.setParametersExpressions(arguments);
        }
    }

    public void popArgumentsClassInstantiation(){
        if(peek() instanceof  LLIRMethodCall){
            LLIRMethodCall methodCall = (LLIRMethodCall) this.llirStack.pop();
            int numberArguments = methodCall.getParametersTable().getSize();
            for (int i = 0; i < numberArguments; i++) {
                this.llirStack.pop();
            }
            if(peek() instanceof  LLIRClassVariableInstantiation){
                LLIRClassVariableInstantiation classVariableInstantiation = (LLIRClassVariableInstantiation) this.llirStack.pop();
                methodCall.setClassVariableInstantiation(classVariableInstantiation);
                addMethodCall(methodCall);
            }
        }
    }


    public void popArgumentsClassInstantiation(int parametersNumber){
        if(peek() instanceof LLIRClassVariableInstantiation){
            LLIRClassVariableInstantiation classVariableInstantiation = (LLIRClassVariableInstantiation)this.llirStack.pop();
            for(int i = 0; i < parametersNumber; i++){
                LLIRExpression expression = (LLIRExpression)this.llirStack.pop();
                classVariableInstantiation.addParameter(expression);
            }
            this.llirStack.push(classVariableInstantiation);
        }
    }

    public void popFunctionCallFunction(){

        List<LLIRExpression> arguments = new ArrayList<>();

        if(this.llirStack.size() > 1){
            if(peek() instanceof LLIRMethodCall) {

                LLIRMethodCall mc = (LLIRMethodCall) this.llirStack.pop();
                if(peek() instanceof LLIRMethodCall){
                    LLIRMethodCall actual = (LLIRMethodCall) peek();
                    arguments.add(mc);
                    actual.setParametersExpressions(arguments);
                }
                else if(peek() instanceof LLIRIfElseBlock) {
                    LLIRIfElseBlock ifElseBlock = (LLIRIfElseBlock) peek();
                    mc.setIsolated(true);
                    ifElseBlock.addNode(mc);
                }
                else if(peek() instanceof LLIRWhileBlock) {
                    LLIRWhileBlock whileBlock = (LLIRWhileBlock) peek();
                    mc.setIsolated(true);
                    whileBlock.addNode(mc);
                }
                
            }else if(peek() instanceof LLIRImport){
                LLIRImport mc = (LLIRImport) this.llirStack.pop();
                if(peek() instanceof LLIRMethodCall){
                    LLIRMethodCall actual = (LLIRMethodCall) peek();
                    arguments.add(mc);
                    actual.setParametersExpressions(arguments);
                }
                else if(peek() instanceof LLIRIfElseBlock) {
                    LLIRIfElseBlock ifElseBlock = (LLIRIfElseBlock) peek();
                    mc.setIsolated(true);
                    ifElseBlock.addNode(mc);
                }
                else if(peek() instanceof LLIRWhileBlock) {
                    LLIRWhileBlock whileBlock = (LLIRWhileBlock) peek();
                    mc.setIsolated(true);
                    whileBlock.addNode(mc);
                }
            }
        }
    }


    //Adds a statement to the stack
    public void addStatement(FunctionDescriptor currentFunctionDescriptor){
        if(!llirStack.empty()) {
            LLIRNode node = this.llirStack.pop();
            if (peek() instanceof LLIRIfElseBlock) {
                LLIRIfElseBlock ifElseBlock = (LLIRIfElseBlock) peek();
                if (!ifElseBlock.isFinishedElse() && ifElseBlock.getExpression() != null) {
                    ifElseBlock.addNode(node);
                    return;
                }
            } else if (peek() instanceof LLIRWhileBlock) {
                LLIRWhileBlock whileBlock = (LLIRWhileBlock) peek();
                if (!whileBlock.isFinished() && whileBlock.getExpression() != null) {
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

    //Pops If and while Blocks from the stack
    public void popBlock(){
        if(peek() instanceof LLIRIfElseBlock) {
            LLIRIfElseBlock node = (LLIRIfElseBlock) this.llirStack.peek();
            node.setFinishedElse(true);
        } else if(peek() instanceof LLIRWhileBlock) {
            LLIRWhileBlock node = (LLIRWhileBlock) this.llirStack.peek();
            node.setFinished(true);
        }
    }

    //Pops if and While expressions from the stack
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

    //True if the top of the stack is an arithmetic expression
    public boolean lastIsArithmetic(){
        if (this.peek() instanceof LLIRArithmetic) return true;
        return false;
    }
    //True if the top of the stack is an conditional expression
    public boolean lastIsConditional(){
        if(this.peek() instanceof LLIRConditional) return true;
        return false;
    }

    public boolean lastIsLLIRExpression(){
        if (this.peek() instanceof LLIRExpression) return true;
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

    //Gets a String containing all the statements from a Block, If or while
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

    //Prints the stack in a nice format
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
            return identation + "Integer" +((LLIRInteger) node).getValue() + "\n";
        }else if(node instanceof LLIRArrayInstantiation){
            return identation +"Array Instantiation\n";
        }else if(node instanceof LLIRArrayAccess){
            return identation +"Array Access\n";
        }else if(node instanceof LLIRArrayLength){
            return identation + "Array Length\n";
        }else if (node instanceof LLIRMethodCall) {
            return identation +"Method Call\n";
        }else if(node instanceof LLIRImport){
            return identation +"Import\n";
        }else if(node instanceof LLIRClassVariable){
            return identation +"Class Variable\n";
        }else if(node instanceof LLIRClassVariableInstantiation){
            return identation +"Class Variable Instantiation\n";
        }else if(node instanceof LLIRNegation){
            return identation +"Negation\n";
        }else if(node instanceof LLIRClassVariableInstantiation){
            return identation + "Class Variable Instantiation\n";
        }else if(node instanceof LLIRExpression){
            return identation +"Expression\n";
        }else if(node instanceof LLIRReturn){
            return identation +"Return\n";
        }
        return "NODE\n";
    }






}
