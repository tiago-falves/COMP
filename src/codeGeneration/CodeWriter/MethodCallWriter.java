package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionParameters;
import llir.LLIRExpression;
import llir.LLIRMethodCall;

import java.util.List;

public class MethodCallWriter {
    private static String LOAD = "\taload_0\n";
    private static String INSTRUCTION = "\tinvokevirtual ";
    private String code;

    public MethodCallWriter(LLIRMethodCall methodCall){
        this.code = LOAD;
        this.code += getParameters(methodCall);
        this.code += INSTRUCTION;

        String arguments = FunctionParameters.getParametersTypes(methodCall.getParametersTable());

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
