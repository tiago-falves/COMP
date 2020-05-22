package llir;

import symbols.ClassDescriptor;
import symbols.Type;

public class LLIRArrayInstantiation extends LLIRExpression {
    private Type type = Type.INT_ARRAY;
    private LLIRExpression size;


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LLIRExpression getSize() {
        return size;
    }

    public void setSize(LLIRExpression size) {
        this.size = size;
    }
}