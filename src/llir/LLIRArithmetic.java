package llir;

public class LLIRArithmetic extends LLIRExpression {
    private ArithmeticOperation operation;
    private LLIRExpression leftExpression;
    private LLIRExpression rightExpression;
    private boolean foundOperator;


    public LLIRArithmetic(ArithmeticOperation op, LLIRExpression left, LLIRExpression right) {
        this.operation = op;
        this.leftExpression = left;
        this.rightExpression = right;
    }

    public LLIRArithmetic(){
        this.foundOperator = false;

    }

    /**
     * @return the operation
     */
    public ArithmeticOperation getOperation() {
        return operation;
    }

    /**
     * @return the leftExpression
     */
    public LLIRExpression getLeftExpression() {
        return leftExpression;
    }

    /**
     * @return the rightExpression
     */
    public LLIRExpression getRightExpression() {
        return rightExpression;
    }

    public void setOperation(ArithmeticOperation operation) {
        this.operation = operation;
        this.foundOperator = true;
    }

    public void setLeftExpression(LLIRExpression leftExpression) {
        this.leftExpression = leftExpression;
    }

    public void setRightExpression(LLIRExpression rightExpression) {
        this.rightExpression = rightExpression;
    }

    public void setExpression(LLIRExpression expression){
        if (foundOperator) {
            setRightExpression(expression);
        }
        else {
            setLeftExpression(expression);
        }

    }

    public boolean foundOperator(){
        return this.foundOperator;
    }

    public void setFoundOperator(boolean foundOperator){
        this.foundOperator = foundOperator;
    }

    
}