package codeGeneration;

public class ClassHeader {
    private static String INSTRUCTION = ".class public ";
    private String className;

    // Receives the name of a class
    public ClassHeader(String className) {
        this.className = className;
    }

    public String generate() {
        String generatedCode = INSTRUCTION + this.className;
        return generatedCode;
    }
}