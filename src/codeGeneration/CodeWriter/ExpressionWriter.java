package codeGeneration.CodeWriter;

import llir.*;
import symbols.Type;

import java.util.List;

public class ExpressionWriter {

    private String code;
    public ExpressionWriter(LLIRExpression expression,String name){
        this.code  = "";


        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression);
            this.code += integerWriter.getCode();
        }
        else if (expression instanceof LLIRBoolean) {
            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean) expression);
            this.code += booleanWriter.getCode();

        }
        else if(expression instanceof LLIRVariable) {
            VariableWriter variableWriter = new VariableWriter((LLIRVariable) expression);
            this.code += variableWriter.getCode();
        }

        else if (expression instanceof LLIRArithmetic) {
            ArithmeticTransformer transformer = new ArithmeticTransformer((LLIRArithmetic) expression);
            LLIRArithmetic transformed = transformer.transform();

            ArithmeticWriter arithmeticWriter = new ArithmeticWriter(transformed,name);
            this.code += arithmeticWriter.getCode();
        }

        else if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            this.code += methodCallWriter.getCode();

        }
        else if (expression instanceof LLIRConditional){
            ConditionalWriter conditionalWriter = new ConditionalWriter((LLIRConditional) expression, name);
            this.code += conditionalWriter.getCode();
        }
        else if (expression instanceof LLIRNegation){
            NegationWriter negationWriter = new NegationWriter((LLIRNegation)expression, name);
            this.code += negationWriter.getCode();
        }else if (expression instanceof LLIRParenthesis) {
            ExpressionWriter expressionWriter = new ExpressionWriter(((LLIRParenthesis) expression).getExpression(),name);
            this.code += expressionWriter.getCode();
        }
    }

    public String getCode(){
        return this.code;
    }
}
