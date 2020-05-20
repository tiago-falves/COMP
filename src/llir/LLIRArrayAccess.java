package llir;

public class LLIRArrayAccess extends LLIRExpression {
    private LLIRExpression array;
    private LLIRExpression access;

    public LLIRArrayAccess(LLIRExpression array, LLIRExpression access) {
        this.array = array;
        this.access = access;
    }

    public LLIRArrayAccess(LLIRExpression array) {
        this.array = array;
    }

    public LLIRArrayAccess() {

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

    public void setArray(LLIRExpression array) {
        this.array = array;
    }

    public void setAccess(LLIRExpression access) {
        this.access = access;
    }

}