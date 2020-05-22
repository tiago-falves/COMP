package llir;

import symbols.NamedTypeDescriptor;

public class LLIRVariableAndArray extends LLIRExpression {
    public NamedTypeDescriptor variable;

    public LLIRVariableAndArray() {
    }

    public LLIRVariableAndArray(NamedTypeDescriptor variable) {
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