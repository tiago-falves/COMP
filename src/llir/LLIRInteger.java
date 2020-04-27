package llir;

public class LLIRInteger extends LLIRExpression {
    private int value;

    public LLIRInteger(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
}