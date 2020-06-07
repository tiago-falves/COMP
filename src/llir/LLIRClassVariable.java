package llir;

import symbols.ClassDescriptor;

public class LLIRClassVariable extends LLIRExpression {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}