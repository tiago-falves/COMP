package llir;

public class LLIRReturn extends LLIRNode {
    LLIRExpression expression;

    public LLIRReturn(LLIRExpression expr) {
        this.expression = expr;
    }
    public LLIRReturn() {

    }

    /**
     * @return the expression
     */
    public LLIRExpression getExpression() {
        return expression;
    }
    public void setExpression(LLIRExpression expression){this.expression = expression;}
}