package llir;

import symbols.VariableDescriptor;

public class LLIRAssignment extends LLIRNode {
    LLIRVariable variable;
    LLIRExpression expression;

    public LLIRAssignment(LLIRVariable var, LLIRExpression expr) {
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