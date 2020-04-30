package codeGeneration.CodeWriter;

import llir.*;
import symbols.Type;

import java.util.List;

public class ExpressionWriter {

    private String code;
    public ExpressionWriter(LLIRExpression expression,String name){
        this.code  = "";


        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression,name);
            this.code += integerWriter.getCode();
        }
        else if (expression instanceof LLIRBoolean) {
            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean) expression,name);
            this.code += booleanWriter.getCode();

        }
        else if(expression instanceof LLIRVariable) {
            VariableWriter variableWriter = new VariableWriter((LLIRVariable) expression);
            this.code += variableWriter.getCode();
        }

        else if (expression instanceof LLIRArithmetic) {
            ArithmeticWriter arithmeticWriter = new ArithmeticWriter((LLIRArithmetic) expression,name);
            this.code += arithmeticWriter.getCode();
        }

        else if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            this.code += methodCallWriter.getCode();

        }
    }

    public String getCode(){
        return this.code;
    }
}
