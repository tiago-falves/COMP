package llir;

import symbols.ClassDescriptor;
import symbols.Type;

public class LLIRArrayInstantiation extends LLIRExpression {
    private Type type;
    private LLIRExpression size;

    public LLIRArrayInstantiation(Type type, LLIRExpression size) {
        this.type = type;
        this.size = size;
    }

    public Type getName() {
        return type;
    }

    public void setName(Type type) {
        this.type = type;
    }

    public LLIRExpression getSize() {
        return size;
    }

    public void setSize(LLIRExpression size) {
        this.size = size;
    }
}