package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ArithmeticWriter {
    private String code;

    public ArithmeticWriter(LLIRArithmetic arithmetic, String name){
        this.code  = "";

        this.code += generateCode(arithmetic.getLeftExpression(),name);
        this.code += generateCode(arithmetic.getRightExpression(),name);


    }
    public String generateCode(LLIRExpression expression,String name){

        String result = new String();
        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression,name);
            result += integerWriter.getCode();
        }
        else if(expression instanceof LLIRVariable) {
            //IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression,name);
            //result += integerWriter.getCode();
        }
        else if (expression instanceof LLIRArithmetic) {
            ArithmeticWriter arithmeticWriter = new ArithmeticWriter((LLIRArithmetic) expression,name);
            result += arithmeticWriter.getCode();
        }
        return result;

    }

    public String getCode(){
        return this.code;
    }
}
