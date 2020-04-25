package llir;

public class LLIRLength extends LLIRExpression {
    private LLIRExpression expression;

    public LLIRLength(LLIRExpression expr) {
        this.expression = expr;
    }

    /**
     * @return the expression
     */
    public LLIRExpression getExpression() {
        return expression;
    }

}