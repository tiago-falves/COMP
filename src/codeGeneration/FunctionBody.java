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
    private int currentVariableIndex;
    private int currentOperationIndex = 0;
    
    public FunctionBody(FunctionDescriptor functionDescriptor, LinkedHashMap<String, Integer> variableToIndex) {
        this.functionDescriptor = functionDescriptor;
        this.variableToIndex = variableToIndex;
        this.currentVariableIndex = variableToIndex.size();
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

        for(LLIRNode node : this.functionDescriptor.getFunctionBody()) {
            if (node instanceof LLIRAssignment) {
                generatedCode += this.generateAssignment((LLIRAssignment) node);
            }
            else if (node instanceof LLIRMethodCall) {

            }
        }
        
        return generatedCode;
    }

    public String generateAssignment(LLIRAssignment assignment) {
        String generatedCode = "";

        LLIRVariable variable = assignment.getVariable();
        LLIRExpression expression = assignment.getExpression();

        if(expression instanceof LLIRInteger) {
            LLIRInteger integer = (LLIRInteger) expression;
            int value = integer.getValue();
            if(value <= 5) {
                generatedCode += "\ticonst_";
                generatedCode += value;
                generatedCode += "\n";
            } else {
                generatedCode += "\tbipush\t";
                generatedCode += value;
                generatedCode += "\n"; 
            }
            this.currentOperationIndex++;

            generatedCode += CodeGeneratorConstants.store.get(Type.INT);
            this.currentOperationIndex = 0;

            int variableIndex = variableToIndex.computeIfAbsent(
                variable.getVariable().getName(), 
                val -> {
                    currentVariableIndex++;
                    return currentVariableIndex;
                }
            );
            
            generatedCode += variableIndex;
            generatedCode += "\n";
        }
        else if (expression instanceof LLIRBoolean) {
            LLIRBoolean bool = (LLIRBoolean) expression;
            //generatedCode += CodeGeneratorConstants.load
        }

        return generatedCode;
    }

}
