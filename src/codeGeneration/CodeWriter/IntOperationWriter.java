package codeGeneration.CodeWriter;

import llir.*;

public class IntOperationWriter {
    private String code;
    private LLIRExpression expression;

    public IntOperationWriter(LLIRExpression expression){
        this.code  = "";
        this.expression = expression;
        this.code += generateCode(expression);
    }

    private String generateCode(LLIRExpression expression){
        String result = new String();
        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression);
            result += integerWriter.getCode();
        }
        else if(expression instanceof LLIRVariable) {
            VariableWriter variableWriter = new VariableWriter((LLIRVariable) expression);
            result += variableWriter.getCode();
        }
        else if (expression instanceof LLIRArithmetic) {
            ArithmeticWriter arithmeticWriter = new ArithmeticWriter((LLIRArithmetic) expression);
            result += arithmeticWriter.getCode();
        }
        else if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            result += methodCallWriter.getCode();
        }
        else if (expression instanceof LLIRParenthesis) {
            ExpressionWriter expressionWriter = new ExpressionWriter(((LLIRParenthesis) expression).getExpression());
            result += expressionWriter.getCode();
        }

        return result;
    }

    public String getCode(){
        return this.code;
    }

}
