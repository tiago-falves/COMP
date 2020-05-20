package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ArrayAccessWriter {
    private String code;
    private LLIRArrayAccess arrayAccess;

    public ArrayAccessWriter(LLIRArrayAccess arrayAccess, String name){
        this.code = "";
        this.arrayAccess = arrayAccess;
        this.code += generateArrayCode(arrayAccess.getArray(), name);
        this.code += generateAccessCode(arrayAccess.getAccess(), name);
    }

    public String generateArrayCode(LLIRExpression expression,String name) {
        String result = new String();
        if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            result += methodCallWriter.getCode();
        }
        else if (expression instanceof LLIRArray) {
            ArrayWriter arrayWriter = new ArrayWriter((LLIRArray) expression);
            result += arrayWriter.getCode();
        }

        return result;
    }

    public String generateAccessCode(LLIRExpression expression, String name) {
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
