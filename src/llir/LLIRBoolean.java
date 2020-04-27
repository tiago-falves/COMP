package llir;

public class LLIRBoolean extends LLIRExpression {
    private boolean value;

    public LLIRBoolean(boolean value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public boolean getValue() {
        return value;
    }
}
