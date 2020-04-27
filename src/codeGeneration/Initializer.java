package codeGeneration;

public class Initializer {
    private static String INSTRUCTION_START = ".method public <init>()V\n";
    private static String INSTRUCTION_ALOAD = "\taload_0\n";
    private static String INSTRUCTION_INVOKE = "\tinvokenonvirtual ";
    private static String INSTRUCTION_INIT = "<init>()V\n";
    private static String INSTRUCTION_RETURN = "\treturn\n";
    private static String INSTRUCTION_END = ".end method\n";
    private static String DEFAULT = "java/lang/Object";

    private String superName;

    // Receives the name of a class' superclass
    public Initializer(String superName) {
        if(superName.equals("")) {
            this.superName = DEFAULT;
        }
        else {
            this.superName = superName;
        }
    }

    public String generate() {
        StringBuilder generatedCode = new StringBuilder(); 

        generatedCode.append(INSTRUCTION_START);
        generatedCode.append(INSTRUCTION_ALOAD);
        generatedCode.append(INSTRUCTION_INVOKE);
        generatedCode.append(this.superName);
        generatedCode.append(INSTRUCTION_INIT);
        generatedCode.append(INSTRUCTION_RETURN);
        generatedCode.append(INSTRUCTION_END);

        return generatedCode.toString();
    }

}