package llir;

public class LLIRReturn extends LLIRNode {
    LLIRExpression expression;

    public LLIRReturn(LLIRExpression expr) {
        this.expression = expr;
    }
    
    /**
     * @return the expression
     */
    public LLIRExpression getExpression() {
        return expression;
    }
}