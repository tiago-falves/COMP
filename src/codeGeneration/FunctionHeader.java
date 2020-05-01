package codeGeneration;

import java.util.LinkedHashMap;

import symbols.FunctionDescriptor;

public class FunctionHeader {
    private static String INSTRUCTION = ".method public ";
    private static String STATIC = "static ";
    private String methodName;
    private String methodDescriptor;
    private String returnType;
    private String STACK_LIMIT = "\t.limit stack ";
    private String LOCALS_LIMIT = "\t.limit locals ";
    private FunctionParameters functionParameters;
    private FunctionDescriptor functionDescriptor;


    public FunctionHeader(FunctionDescriptor function){
        this.functionParameters = new FunctionParameters(function);
        this.methodName = function.getName();
        this.methodDescriptor = functionParameters.generate();
        this.returnType = CGConst.getJvmType(function);
        this.functionDescriptor = function;

    }

    public LinkedHashMap<String, Integer> getParameters() {
        return this.functionParameters.getParameters();
    }

    public String generate() {
        String generatedCode = INSTRUCTION;
        if(functionDescriptor.isStatic()) generatedCode+=STATIC;
        generatedCode += methodName + "(" + methodDescriptor + ")" + returnType + "\n";
        generatedCode += STACK_LIMIT + "\n" + LOCALS_LIMIT + "\n";
        return generatedCode;
    }

}