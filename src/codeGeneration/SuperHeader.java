package codeGeneration;

public class SuperHeader {
    private static String INSTRUCTION = ".super ";
    private static String DEFAULTSUPER = "java/lang/Object";
    private String superName;

    // Receives the name of a class' superclass
    public SuperHeader(String superName) {
        if(superName.equals("")) {
            this.superName = DEFAULTSUPER;
        }
        else {
            this.superName = superName;
        }
    }

    public String generate() {
        String generatedCode = INSTRUCTION + this.superName;
        return generatedCode;
    }
}