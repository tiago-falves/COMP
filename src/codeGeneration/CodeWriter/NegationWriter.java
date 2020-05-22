package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class NegationWriter {
    private String code;
    private LLIRNegation negation;

    public NegationWriter(LLIRNegation negation){
        this.code  = "";
        this.negation = negation;
        this.code += generateCode(negation.getExpression());
        this.code += CGConst.TRUE_VALUE + "\n";
        FunctionBody.incStack();
        this.code += "\t" + "ixor";
        this.code += "\n";
    }

    private String generateCode(LLIRExpression expression){

        String result = new String();
        if(expression instanceof LLIRVariable) {
            VariableWriter variableWriter = new VariableWriter((LLIRVariable) expression);
            result += variableWriter.getCode();
        }
        else if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            result += methodCallWriter.getCode();
        }
        else if (expression instanceof LLIRParenthesis) {
            ExpressionWriter expressionWriter = new ExpressionWriter(((LLIRParenthesis) expression).getExpression());
            result += expressionWriter.getCode();
        }
        else if (expression instanceof LLIRBoolean) {
            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean)expression);
            result += booleanWriter.getCode();
        }

        return result;
    }

    public String getCode(){
        return this.code;
    }

}
