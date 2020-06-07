package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import codeGeneration.FunctionParameters;
import llir.LLIRConditional;
import llir.LLIRExpression;
import llir.LLIRMethodCall;
import llir.LLIRNegation;
import optimizations.ConstantFoldingConditional;
import optimizations.ConstantFoldingNegation;
import optimizations.OptimizationManager;
import optimizations.RegisterReducer;
import symbols.SymbolsTable;
import symbols.Type;

import java.util.List;

public class MethodCallWriter {
    private static String LOAD = "\taload";
    private static String INSTRUCTION = "\tinvokevirtual ";
    private static String POP = "\tpop";
    private String code = "";

    public MethodCallWriter(LLIRMethodCall methodCall){

        if(methodCall.getClassVariableInstantiation() != null){
            ClassVariableInstantiationWriter classVariableInstantiationWriter = new ClassVariableInstantiationWriter(methodCall.getClassVariableInstantiation(), true);
            this.code += classVariableInstantiationWriter.getCode();
        }
        else if (methodCall.getClassName() == "") {
            this.code = LOAD + "_0\n";
        }else{
            boolean foundVariable = false;

            if(OptimizationManager.reducedLocals && !RegisterReducer.firstPass) {
                String variableName = methodCall.getClassName();

                if(RegisterReducer.allocation.containsKey(variableName)) {
                    int register = RegisterReducer.allocation.get(variableName);
                    this.code += LOAD + FunctionBody.getVariableIndexOptimized(register) + "\n";
                    foundVariable = true;
                }   
            }

            if(!foundVariable) {
                String variableIndex = FunctionBody.getVariableIndexString(methodCall.getClassName());
                this.code = LOAD + variableIndex + "\n";
            }

            if(OptimizationManager.reducedLocals && RegisterReducer.firstPass)
                RegisterReducer.addUse(methodCall.getClassName());
        }
        FunctionBody.incStack();

        this.code += getParameters(methodCall);
        this.code += INSTRUCTION;

        String arguments = FunctionParameters.getParametersTypes(methodCall.getParametersTable());
        //TODO Aceder ao nome da classe sem fazer isto do Upper Case
        if (methodCall.getClassType() != "")
            this.code += methodCall.getClassType() + "/";
        else if (methodCall.getClassName()!="") 
            this.code+= methodCall.getClassName().substring(0, 1).toUpperCase() + methodCall.getClassName().substring(1) + "/";
        if (methodCall.getClassVariableInstantiation()!=null) 
            this.code+= methodCall.getClassVariableInstantiation().getName()+ "/";


        this.code += methodCall.getMethodName() + "(" + arguments + ")"+ CGConst.types.get(methodCall.getReturnType()) + "\n";
        FunctionBody.decStack(1 + methodCall.getParametersTable().getSize()-(methodCall.getReturnType() == Type.VOID ? 0 : 1));

        if(methodCall.isIsolated()) {
            this.code += POP + "\n";
            FunctionBody.decStack(1);
        }
    }

    public String getCode(){
        return this.code;
    }

    private String getParameters(LLIRMethodCall methodCall){
        String result = "";

        List<LLIRExpression> parameters = methodCall.getParametersExpressions();
        for (LLIRExpression expression : parameters){
            if(OptimizationManager.constantFolding && expression instanceof LLIRConditional){
                LLIRConditional assignmentConditional = (LLIRConditional)expression;
                ConstantFoldingConditional constantFoldingConditional = new ConstantFoldingConditional(assignmentConditional);
                expression = constantFoldingConditional.getConditional();
            }else if(OptimizationManager.constantFolding && expression instanceof LLIRNegation){
                LLIRNegation assignmentNegation = (LLIRNegation)expression;
                ConstantFoldingNegation constantFoldingNegation = new ConstantFoldingNegation(assignmentNegation);
                expression = constantFoldingNegation.getNegation();
            }
            ExpressionWriter expressionWriter = new ExpressionWriter(expression);
            result += expressionWriter.getCode();
        }

        return result;
    }
}
