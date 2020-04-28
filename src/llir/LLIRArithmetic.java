package llir;

public class LLIRArithmetic extends LLIRExpression {
    private ArithmeticOperation operation;
    private LLIRExpression leftExpression;
    private LLIRExpression rightExpression;


    public LLIRArithmetic(ArithmeticOperation op, LLIRExpression left, LLIRExpression right) {
        this.operation = op;
        this.leftExpression = left;
        this.rightExpression = right;
    }

    public LLIRArithmetic(){

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
    }

    public void setLeftExpression(LLIRExpression leftExpression) {
        this.leftExpression = leftExpression;
    }

    public void setRightExpression(LLIRExpression rightExpression) {
        this.rightExpression = rightExpression;
    }

    public void setExpression(LLIRExpression expression,boolean foundOperator){
        if (foundOperator) setRightExpression(expression);
        else setLeftExpression(expression);

    }
    
}