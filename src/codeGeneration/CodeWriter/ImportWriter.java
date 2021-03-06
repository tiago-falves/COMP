package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import codeGeneration.FunctionParameters;
import llir.LLIRExpression;
import llir.LLIRImport;
import llir.LLIRMethodCall;
import optimizations.OptimizationManager;
import optimizations.RegisterReducer;
import symbols.Type;

import java.util.List;
import java.util.function.Function;

public class ImportWriter {
    private static String INSTRUCTION = "\tinvokestatic ";
    private static String LOAD = "\taload";
    private String code;
    private static String POP = "\tpop";

    public ImportWriter(LLIRImport importLLIR){
        this.code ="";
        
        if(!importLLIR.getImportDescriptor().isStatic()){
            if(importLLIR.getVariableName() == "")
                this.code = LOAD + "_0\n";
            else{
                boolean foundVariable = false;

                if(OptimizationManager.reducedLocals && !RegisterReducer.firstPass) {
                    String variableName = importLLIR.getVariableName();

                    if(RegisterReducer.allocation.containsKey(variableName)) {
                        int register = RegisterReducer.allocation.get(variableName);
                        this.code += LOAD + FunctionBody.getVariableIndexOptimized(register) + "\n";
                        foundVariable = true;
                    }   
                }

                if(!foundVariable) {
                    String variableIndex = FunctionBody.getVariableIndexString(importLLIR.getVariableName());
                    this.code = LOAD + variableIndex + "\n";
                }
            }
            FunctionBody.incStack();
        }

        this.code += getParameters(importLLIR);

        if(importLLIR.getImportDescriptor().isStatic())
            this.code += INSTRUCTION;
        else 
            this.code += "\tinvokevirtual ";

        String arguments = FunctionParameters.getParametersTypes(importLLIR.getImportDescriptor().getParameters());
        this.code += getIdentifiers(importLLIR) + "(" + arguments + ")"+ CGConst.types.get(importLLIR.getImportDescriptor().getReturn()) + "\n";
        FunctionBody.decStack(1 + importLLIR.getImportDescriptor().getParameters().size() - (importLLIR.getImportDescriptor().getReturn() == Type.VOID?0:1));
        
        if(importLLIR.isIsolated() && importLLIR.getImportDescriptor().getType() != Type.VOID) {
            this.code += POP + "\n";
            FunctionBody.decStack(1);
        }
    }

    private String getIdentifiers(LLIRImport importLLIR){
        String genarated ="";
        List<String> identifiers =  importLLIR.getImportDescriptor().getIdentifiers();
        for (String identifier : identifiers){
            genarated += identifier + "/";
        }
        genarated = genarated.substring(0, genarated.length() - 1);
        return genarated;

    }

    public String getCode(){
        return this.code;
    }

    private String getParameters(LLIRImport importLLIR){
        String result = "";
        List<LLIRExpression> parameters = importLLIR.getParametersExpressions();
        for (LLIRExpression expression : parameters){
            ExpressionWriter expressionWriter = new ExpressionWriter(expression);
            result += expressionWriter.getCode();

        }

        return result;
    }
}
