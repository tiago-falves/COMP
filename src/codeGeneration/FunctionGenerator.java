package codeGeneration;

import symbols.FunctionDescriptor;
import symbols.SymbolsTable;

public class FunctionGenerator {
    private FunctionDescriptor functionDescriptor;
    private SymbolsTable fieldsTable;


    public FunctionGenerator(FunctionDescriptor functionDescriptor, SymbolsTable fieldTable) {
        this.functionDescriptor = functionDescriptor;
        this.fieldsTable = fieldTable;
    }

    public String generate(){

        FunctionHeader functionHeader = new FunctionHeader(functionDescriptor);
        FunctionBody functionBody = new FunctionBody(functionDescriptor, functionHeader.getParameters(),fieldsTable);
        FunctionFooter footer = new FunctionFooter();

        return functionHeader.generate() + functionBody.generate() + footer.generate();
    }


}
