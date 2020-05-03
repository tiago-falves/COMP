package llir;

public class LLIRStatement extends LLIRNode {
    LLIRVariable variable;
    LLIRExpression expression;

    public LLIRStatement(LLIRVariable var, LLIRExpression expr) {
        this.variable = var;
        this.expression = expr;
    }

    /**
     * @return the variable
     */
    public LLIRVariable getVariable() {
        return variable;
    }

    /**
     * @return the expression
     */
    public LLIRExpression getExpression() {
        return expression;
    }
}