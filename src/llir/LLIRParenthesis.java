package llir;

public class LLIRParenthesis extends LLIRExpression {
    private LLIRExpression expression;

    public LLIRParenthesis(LLIRExpression expression) {
        this.expression = expression;
    }

    public LLIRParenthesis(){

    }


    public LLIRExpression getExpression() {
        return expression;
    }

    public void setExpression(LLIRExpression expression) {
        this.expression = expression;
    }
}
