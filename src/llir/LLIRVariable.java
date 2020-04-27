package llir;

import symbols.VariableDescriptor;

public class LLIRVariable extends LLIRExpression {
    public VariableDescriptor variable;

    public LLIRVariable(VariableDescriptor variable) {
        this.variable = variable;
    }

    /**
     * @param variable the variable to set
     */
    public void setVariable(VariableDescriptor variable) {
        this.variable = variable;
    }

    /**
     * @return the variable
     */
    public VariableDescriptor getVariable() {
        return variable;
    }
}  