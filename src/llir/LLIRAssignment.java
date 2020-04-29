package llir;

import symbols.VariableDescriptor;

public class LLIRAssignment extends LLIRNode {
    LLIRVariable variable;
    LLIRExpression expression;

    public LLIRAssignment() {
        
    }

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
     * @param variable the variable to set
     */
    public void setVariable(LLIRVariable variable) {
        this.variable = variable;
    }

    /**
     * @return the expression
     */
    public LLIRExpression getExpression() {
        return expression;
    }

    /**
     * @param expression the expression to set
     */
    public void setExpression(LLIRExpression expression) {
        this.expression = expression;
    }


}