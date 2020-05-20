package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

import static llir.ConditionalOperation.*;

public class ConditionalWriter {
    private String code;
    private LLIRConditional conditional;

    private static int conditionalGotoNumber = 0;

    public ConditionalWriter(LLIRConditional conditional, String name){
        this.code  = "";
        this.conditional = conditional;
        this.code += generateCode(conditional.getLeftExpression(),name);     // left
        this.code += generateCode(conditional.getRightExpression(),name);    // right

        switch(conditional.getOperation()){
            case AND:
                this.code += "\tiand";
                break;
            case LESS_THAN:

                this.code += "\t" + "if_icmpge notLess_" + conditionalGotoNumber + "\n";
                this.code += CGConst.TRUE_VALUE + "\n";
                this.code += "\t" + "goto endLess_" + conditionalGotoNumber + "\n";
                this.code += "notLess_" + conditionalGotoNumber + ":" + "\n";
                this.code += CGConst.FALSE_VALUE + "\n";
                this.code += "endLess_" + conditionalGotoNumber + ":";
                conditionalGotoNumber++;
                break;
        }
        this.code += "\n";
    }

    private String generateCode(LLIRExpression expression,String name){

        String result = new String();

        ExpressionWriter expressionWriter = new ExpressionWriter(expression,name);
        result += expressionWriter.getCode();
        /*if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression);
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
        else if (expression instanceof LLIRBoolean) {
            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean)expression);
            result += booleanWriter.getCode();
        }
        else if(expression instanceof LLIRNegation){
            NegationWriter negationWriter = new NegationWriter((LLIRNegation)expression, name);
            result += negationWriter.getCode();
        }*/

        return result;
    }

    public String getCode(){
        return this.code;
    }

}
