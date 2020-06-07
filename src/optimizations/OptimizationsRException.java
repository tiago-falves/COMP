package optimizations;

public class OptimizationsRException extends Exception {
    
    public OptimizationsRException(){
        super("Compilation failed due to lack of registers");
    } 
}