package llir;

public class LLIRArrayAccess extends LLIRVariableAndArray {
    private LLIRExpression array;
    private LLIRExpression access;

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