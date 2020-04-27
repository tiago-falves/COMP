package codeGeneration;

public class FunctionHeader {
    private static String INSTRUCTION = ".method public ";
    private String methodName;
    private String methodDescriptor;
    private String returnType;
    private String STACK_LIMIT = "\t.limit stack ";
    private String LOCALS_LIMIT = "\t.limit locals ";

    // Receives the name of a class
    public FunctionHeader(String methodName, String methodDescriptor, String returnType) {
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.returnType = returnType;
    }

    public String generate() {
        String generatedCode = INSTRUCTION + "\n";
        generatedCode += methodName + "(" + methodDescriptor + ")" + returnType + "\n";
        generatedCode += STACK_LIMIT + "\n" + LOCALS_LIMIT + "\n";
        return generatedCode;
    }

}