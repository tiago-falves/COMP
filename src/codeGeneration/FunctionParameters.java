package codeGeneration;

import symbols.Descriptor;
import symbols.FunctionDescriptor;
import symbols.FunctionParameterDescriptor;
import symbols.SymbolsTable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class FunctionParameters {
    private FunctionDescriptor functionDescriptor;

    // Receives the name of a class
    public FunctionParameters(FunctionDescriptor functionDescriptor) {
        this.functionDescriptor = functionDescriptor;
    }

    public String generate() {
        String parameters = new String();

        SymbolsTable parametersTable = functionDescriptor.getParametersTable();
        LinkedHashMap<String, List<Descriptor>> table = parametersTable.getTable();
        Set<String> keys = table.keySet();

        for(String k:keys){
            List<Descriptor> functionDescriptors = table.get(k);
            for (Descriptor descriptor : functionDescriptors){
                FunctionParameterDescriptor parameterDescriptor= (FunctionParameterDescriptor) descriptor;
                String parameter = generateMethodParameter(parameterDescriptor);
                parameters=  parameters.concat(parameter);

            }
        }
        return parameters;
    }

    private String generateMethodParameter(FunctionParameterDescriptor parameterDescriptor) {
        return  CodeGeneratorConstants.getJvmType(parameterDescriptor);
    }





}