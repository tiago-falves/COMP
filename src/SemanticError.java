public class SemanticError {

    private final int maxNumErrors;

    private int numErrors;

    public SemanticError(){
        this(10);
    }

    public SemanticError(int maxNumErrors){
        this.maxNumErrors = maxNumErrors;
        this.numErrors = 0;
    }

    public void printError(SimpleNode node, String message) throws SemanticErrorException {
        this.numErrors++;
        System.err.println(message);
        
        if(numErrors >= maxNumErrors)
            throw new SemanticErrorException();
    }
}