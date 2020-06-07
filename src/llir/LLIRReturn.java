package llir;

import symbols.Type;

public class LLIRReturn extends LLIRNode {
    LLIRExpression expression;
    private Type returnType;

    /**
     * @return the expression
     */
    public LLIRExpression getExpression() {
        return expression;
    }
    public void setExpression(LLIRExpression expression){this.expression = expression;}

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
}