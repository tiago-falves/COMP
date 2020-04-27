package codeGeneration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import llir.*;
import symbols.FunctionDescriptor;
import symbols.Type;

public class FunctionBody {
    private FunctionDescriptor functionDescriptor;
    private LinkedHashMap<String, Integer> variableToIndex;
    private int currentIndex;
    
    public FunctionBody(FunctionDescriptor functionDescriptor, LinkedHashMap<String, Integer> variableToIndex) {
        this.functionDescriptor = functionDescriptor;
        this.variableToIndex = variableToIndex;
        this.currentIndex = variableToIndex.size();
    }

    private String pushParameters() {
        StringBuilder generatedCode = new StringBuilder();

        for (Map.Entry<String, Integer> entry : variableToIndex.entrySet()) {
            String variableName = entry.getKey();
            Type variableType = this.functionDescriptor.getVariableType(variableName);
            String instruction = CodeGeneratorConstants.load.get(variableType);
            generatedCode.append(instruction + entry.getValue() + "\n");
        }

        return generatedCode.toString();
    }

    public String generate(){
        String generatedCode = this.pushParameters();

        /*
        for(LLIRNode node : this.functionDescriptor.getFunctionBody()) {
            if (node instanceof LLIRAssignment) {
                
            }
            else if (node instanceof LLIRMethodCall) {

            }
        }
        */
        
        return generatedCode;
    }

}
