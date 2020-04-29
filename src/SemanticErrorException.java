public class SemanticErrorException extends Exception {
    
    public SemanticErrorException(){
        super("Compilation failed due to semantic errors");
    }

    public SemanticErrorException(String message){
        super(message);
    } 
}