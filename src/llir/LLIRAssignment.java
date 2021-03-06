package llir;

import symbols.VariableDescriptor;

public class LLIRAssignment extends LLIRNode {
    LLIRVariableAndArray variable;
    LLIRExpression expression;

    /**
     * @return the variable
     */
    public LLIRVariableAndArray getVariable() {
        return variable;
    }

    /**
     * @param variable the variable to set
     */
    public void setVariable(LLIRVariableAndArray variable) {
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