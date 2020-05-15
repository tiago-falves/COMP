package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import codeGeneration.FunctionParameters;
import llir.LLIRExpression;
import llir.LLIRMethodCall;

import java.util.List;

public class MethodCallWriter {
    private static String LOAD = "\taload";
    private static String INSTRUCTION = "\tinvokevirtual ";
    private String code;

    public MethodCallWriter(LLIRMethodCall methodCall){

        if (methodCall.getClassName() == "") this.code = LOAD + "_0\n";
        else{
            String variableIndex = FunctionBody.getVariableIndexString(methodCall.getClassName());
            this.code = LOAD + variableIndex + "\n";
        }

        this.code += getParameters(methodCall);
        this.code += INSTRUCTION;

        String arguments = FunctionParameters.getParametersTypes(methodCall.getParametersTable());
        if (methodCall.getClassName()!="") this.code+= methodCall.getClassName().substring(0, 1).toUpperCase() + methodCall.getClassName().substring(1) + "/";

        this.code += methodCall.getMethodName() + "(" + arguments + ")"+ CGConst.types.get(methodCall.getReturnType()) + "\n";

    }

    public String getCode(){
        return this.code;
    }

    private String getParameters(LLIRMethodCall methodCall){
        String result = "";

        List<LLIRExpression> parameters = methodCall.getParametersExpressions();
        for (LLIRExpression expression : parameters){
            ExpressionWriter expressionWriter = new ExpressionWriter(expression,"NAME");
            result += expressionWriter.getCode();
        }

        return result;
    }
}
