package codeGeneration;

import java.util.HashMap;
import java.util.List;

import symbols.SymbolsTable;
import symbols.Descriptor;
import symbols.VariableDescriptor;

public class Fields {
    private static String INSTRUCTION = ".field private ";
    private static String SPACE = " ";
    private static String NEWLINE = "\n";
    private final SymbolsTable fieldsTable;

    // Receives the variable table of a class
    public Fields(SymbolsTable fieldsTable) {
        this.fieldsTable = fieldsTable;
    }

    public String generateCode() {
        StringBuilder generatedCode = new StringBuilder(); 
        
        for (HashMap.Entry<String, List<Descriptor>> tableEntry : fieldsTable.getTable().entrySet()) {
            if(tableEntry.getValue().size() > 0) {
                VariableDescriptor field = (VariableDescriptor) tableEntry.getValue().get(0);

                generatedCode.append(INSTRUCTION);
                generatedCode.append(field.getName());
                generatedCode.append(SPACE);
                generatedCode.append(field.getType()); // change to get code
                generatedCode.append(NEWLINE);
            }
        }

        return generatedCode.toString();
    }

}