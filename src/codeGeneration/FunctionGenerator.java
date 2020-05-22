package codeGeneration;

import symbols.FunctionDescriptor;

public class FunctionGenerator {
    private FunctionDescriptor functionDescriptor;


    public FunctionGenerator(FunctionDescriptor functionDescriptor) {
        this.functionDescriptor = functionDescriptor;
    }

    public String generate(){

        FunctionHeader functionHeader = new FunctionHeader(functionDescriptor);
        FunctionBody functionBody = new FunctionBody(functionDescriptor, functionHeader.getParameters());
        FunctionFooter footer = new FunctionFooter();

        return functionHeader.generate() + functionBody.generate() + footer.generate();
    }


}
