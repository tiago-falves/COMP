package codeGeneration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import codeGeneration.CodeWriter.AssignmentWriter;
import codeGeneration.CodeWriter.BooleanWriter;
import codeGeneration.CodeWriter.IntegerWriter;
import codeGeneration.CodeWriter.MethodCallWriter;
import llir.*;
import symbols.Descriptor;
import symbols.FunctionDescriptor;
import symbols.Type;


public class FunctionBody {
    private FunctionDescriptor functionDescriptor;
    public static LinkedHashMap<String, Integer> variableToIndex;
    public static int currentVariableIndex;
    public static int currentOperationIndex = 0;
    
    public FunctionBody(FunctionDescriptor functionDescriptor, LinkedHashMap<String, Integer> variableToIndex) {
        this.functionDescriptor = functionDescriptor;
        this.variableToIndex = variableToIndex;
        this.currentVariableIndex = variableToIndex.size();
    }


    private void pushVariables(){
        LinkedHashMap<String, List<Descriptor>> bodyTable = functionDescriptor.getBodyTable().getTable();

        for (Map.Entry<String, List<Descriptor>> entry : bodyTable.entrySet()) {
            String variableName = entry.getKey();
            int variableIndex = getVariableIndex(variableName);
            variableToIndex.put(variableName,variableIndex);
        }


    }

    public String generate(){

        String generatedCode = new String();

        pushVariables();

        for(LLIRNode node : this.functionDescriptor.getFunctionBody()) {
            if (node instanceof LLIRAssignment) {
                AssignmentWriter assignmentWriter = new AssignmentWriter((LLIRAssignment) node);
                generatedCode += assignmentWriter.getCode();
            }
            else if (node instanceof LLIRMethodCall) {
                MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) node);
                generatedCode += methodCallWriter.getCode();

            }
        }
        
        return generatedCode;
    }




    public static int getVariableIndex(String name){
        int variableIndex = variableToIndex.computeIfAbsent(
                name,
                val -> {
                    currentVariableIndex++;
                    return currentVariableIndex;
                }
        );
        return variableIndex;
    }

    //APAGAR
    private String pushParameters() {
        StringBuilder generatedCode = new StringBuilder();

        for (Map.Entry<String, Integer> entry : variableToIndex.entrySet()) {
            String variableName = entry.getKey();
            Type variableType = this.functionDescriptor.getVariableType(variableName);
            String instruction = CGConst.load.get(variableType);
            generatedCode.append(instruction + entry.getValue() + "\n");
        }

        return generatedCode.toString();
    }

}
