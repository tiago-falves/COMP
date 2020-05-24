package codeGeneration;

import symbols.ClassDescriptor;
import symbols.FunctionDescriptor;
import symbols.SymbolsTable;

public class FunctionGenerator {
    private FunctionDescriptor functionDescriptor;
    private ClassDescriptor classDescriptor;
    private SymbolsTable fieldsTable;


    public FunctionGenerator(FunctionDescriptor functionDescriptor, ClassDescriptor classDescriptor) {
        this.functionDescriptor = functionDescriptor;
        this.fieldsTable = classDescriptor.getVariablesTable();
        this.classDescriptor = classDescriptor;
    }

    public String generate(){

        FunctionHeader functionHeader = new FunctionHeader(functionDescriptor);
        FunctionBody functionBody = new FunctionBody(functionDescriptor, functionHeader.getParameters(),classDescriptor);
        FunctionFooter footer = new FunctionFooter();

        return functionHeader.generate() + functionBody.generate() + footer.generate();
    }


}
