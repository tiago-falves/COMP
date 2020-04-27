public class SemanticErrorException extends Exception {
    public SemanticErrorException(){
        super("Reached maximum number of semantic errors");
    } 
}