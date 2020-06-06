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

    public ConditionalWriter(LLIRConditional conditional){
        this.code  = "";
        this.conditional = conditional;
        this.code += generateCode(conditional.getLeftExpression());     // left
        this.code += generateCode(conditional.getRightExpression());    // right

        switch(conditional.getOperation()){
            case AND:
                this.code += "\tiand";
                break;
            case LESS_THAN:

                this.code += "\t" + "if_icmpge notLess_" + conditionalGotoNumber + "\n";
                this.code += CGConst.TRUE_VALUE + "\n";
                //FunctionBody.incStack();
                this.code += "\t" + "goto endLess_" + conditionalGotoNumber + "\n";
                this.code += "notLess_" + conditionalGotoNumber + ":" + "\n";
                this.code += CGConst.FALSE_VALUE + "\n";
                //FunctionBody.incStack();
                this.code += "endLess_" + conditionalGotoNumber + ":";
                conditionalGotoNumber++;
                break;
        }
        this.code += "\n";
    }

    private String generateCode(LLIRExpression expression){

        String result = new String();

        ExpressionWriter expressionWriter = new ExpressionWriter(expression);
        result += expressionWriter.getCode();

        return result;
    }

    public String getCode(){
        return this.code;
    }

}
