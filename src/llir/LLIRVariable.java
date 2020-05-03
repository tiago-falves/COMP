package llir;

import symbols.NamedTypeDescriptor;
import symbols.VariableDescriptor;

public class LLIRVariable extends LLIRExpression {
    public NamedTypeDescriptor variable;

    public LLIRVariable(NamedTypeDescriptor variable) {
        this.variable = variable;
    }

    /**
     * @param variable the variable to set
     */
    public void setVariable(NamedTypeDescriptor variable) {
        this.variable = variable;
    }

    /**
     * @return the variable
     */
    public NamedTypeDescriptor getVariable() {
        return variable;
    }


}