package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ConditionalWriter {
    private String code;
    private LLIRConditional conditional;

    public ConditionalWriter(LLIRConditional conditional, String name){
        this.code  = "";
        this.conditional = conditional;
        this.code += generateCode(conditional.getLeftExpression(),name);     // left
        this.code += generateCode(conditional.getRightExpression(),name);    // right
        this.code += CGConst.arithmeticOperators.get(conditional.getOperation()) + "\n";
    }

    private String generateCode(LLIRExpression expression,String name){

        String result = new String();
        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression,name);
            result += integerWriter.getCode();
        }
        else if(expression instanceof LLIRVariable) {
            VariableWriter variableWriter = new VariableWriter((LLIRVariable) expression);
            result += variableWriter.getCode();
        }
        else if (expression instanceof LLIRArithmetic) {
            ArithmeticWriter arithmeticWriter = new ArithmeticWriter((LLIRArithmetic) expression,name);
            result += arithmeticWriter.getCode();
        }
        else if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            result += methodCallWriter.getCode();
        }
        else if (expression instanceof LLIRParenthesis) {
            ExpressionWriter expressionWriter = new ExpressionWriter(((LLIRParenthesis) expression).getExpression(),name);
            result += expressionWriter.getCode();
        }

        return result;
    }

    public String getCode(){
        return this.code;
    }

}
