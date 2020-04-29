package llir;

public class LLIRArrayAccess extends LLIRExpression {
    private LLIRExpression array;
    private LLIRExpression access;

    public LLIRArrayAccess(LLIRExpression array, LLIRExpression access) {
        this.array = array;
        this.access = access;
    }

    /**
     * @return the access
     */
    public LLIRExpression getAccess() {
        return access;
    }

    /**
     * @return the array
     */
    public LLIRExpression getArray() {
        return array;
    }



}