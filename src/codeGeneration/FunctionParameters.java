package codeGeneration;

import symbols.Descriptor;
import symbols.FunctionDescriptor;
import symbols.FunctionParameterDescriptor;
import symbols.SymbolsTable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FunctionParameters {
    private FunctionDescriptor functionDescriptor;

    // Receives the name of a class
    public FunctionParameters(FunctionDescriptor functionDescriptor) {
        this.functionDescriptor = functionDescriptor;
    }

    public String generate() {
        String parameters = new String();

        SymbolsTable parametersTable = functionDescriptor.getParametersTable();
        if(parametersTable != null) {
            for (HashMap.Entry<String, List<Descriptor>> tableEntry : parametersTable.getTable().entrySet()) {
                if(tableEntry.getValue().size() > 0) {
                    FunctionParameterDescriptor paramDescriptor = (FunctionParameterDescriptor) tableEntry.getValue().get(0);
                    String parameter = generateMethodParameter(paramDescriptor);
                    parameters = parameters.concat(parameter);
                }
            }
        }

        return parameters;
    }

    public LinkedHashMap<String, Integer> getParameters() {
        int currentIndex = 1;
        LinkedHashMap<String, Integer> variableToIndex = new LinkedHashMap<>();

        SymbolsTable parametersTable = functionDescriptor.getParametersTable();
        if(parametersTable != null) {
            for (HashMap.Entry<String, List<Descriptor>> tableEntry : parametersTable.getTable().entrySet()) {
                if(tableEntry.getValue().size() > 0) {
                    FunctionParameterDescriptor paramDescriptor = (FunctionParameterDescriptor) tableEntry.getValue().get(0);
                    variableToIndex.put(paramDescriptor.getName(), currentIndex);
                    currentIndex++;
                }
            }
        }

        return variableToIndex;
    }

    private String generateMethodParameter(FunctionParameterDescriptor parameterDescriptor) {
        return  CGConst.getJvmType(parameterDescriptor);
    }





}