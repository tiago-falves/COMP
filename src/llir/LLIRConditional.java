package llir;

public class LLIRConditional extends LLIRExpression {
    private ConditionalOperation operation;
    private LLIRExpression leftExpression;
    private LLIRExpression rightExpression;
    private boolean foundOperator;


    public LLIRConditional(ConditionalOperation op, LLIRExpression left, LLIRExpression right) {
        this.operation = op;
        this.leftExpression = left;
        this.rightExpression = right;
    }

    public LLIRConditional(){
        this.foundOperator = false;

    }

    /**
     * @return the operation
     */
    public ConditionalOperation getOperation() {
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

    public void setOperation(ConditionalOperation operation) {
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