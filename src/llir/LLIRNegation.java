package llir;

public class LLIRNegation extends LLIRExpression {
    private LLIRExpression expression;

    public LLIRNegation() {
        this.expression = null;
    }

    public LLIRNegation(LLIRExpression expr) {
        this.expression = expr;
    }

    public void setExpression(LLIRExpression expr){
        this.expression = expr;
    }

    /**
     * @return the expression
     */
    public LLIRExpression getExpression() {
        return expression;
    }


}