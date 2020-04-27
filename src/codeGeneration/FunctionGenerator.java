package codeGeneration;

import symbols.FunctionDescriptor;

public class FunctionGenerator {
    private FunctionDescriptor functionDescriptor;


    public FunctionGenerator(FunctionDescriptor functionDescriptor) {
        this.functionDescriptor = functionDescriptor;
    }

    public String generate(){
        String s = "";

        FunctionHeader functionHeader = new FunctionHeader(functionDescriptor);
        FunctionBody functionBody = new FunctionBody(functionDescriptor);
        FunctionFooter footer = new FunctionFooter();


        return functionHeader.generate() + functionBody.generate() + footer.generate();
    }


}
