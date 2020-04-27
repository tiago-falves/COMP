package codeGeneration;

public class FunctionFooter {
    private static String INSTRUCTION = ".end method";

    public String generate() {
        return INSTRUCTION + "\n";
    }

}