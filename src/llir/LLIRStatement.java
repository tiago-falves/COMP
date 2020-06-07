package llir;

public class LLIRStatement extends LLIRNode {
    LLIRVariable variable;
    LLIRExpression expression;

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