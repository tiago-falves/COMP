package llir;

public class LLIRArrayLength extends LLIRVariableAndArray {
    private LLIRExpression array;

    public LLIRArrayLength(){
        this.array = null;
    }
    
    public LLIRArrayLength(LLIRExpression array) {
        this.array = array;
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


}