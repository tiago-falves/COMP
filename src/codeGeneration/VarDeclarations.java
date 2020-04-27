package codeGeneration;

import symbols.Descriptor;
import symbols.FunctionDescriptor;
import symbols.SymbolsTable;
import symbols.VariableDescriptor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class VarDeclarations {
    private FunctionDescriptor functionDescriptor;


    public VarDeclarations(FunctionDescriptor functionDescriptor) {
        this.functionDescriptor = functionDescriptor;
    }
    public String generate(){
        String variables = new String();

        SymbolsTable bodyTable  = functionDescriptor.getBodyTable();
        LinkedHashMap<String, List<Descriptor>> table = bodyTable.getTable();
        Set<String> keys = table.keySet();

        for(String k:keys){
            List<Descriptor> functionDescriptors = table.get(k);
            for (Descriptor descriptor : functionDescriptors){
                VariableDescriptor variableDescriptor= (VariableDescriptor) descriptor;

                VarDeclaration varDeclaration = new VarDeclaration(variableDescriptor);
                String variable = varDeclaration.generate();

                variables=  variables.concat(variable);

            }
        }

        return variables;
    }

}
