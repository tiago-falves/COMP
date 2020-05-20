package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ArithmeticWriter {
    private String code;
    private LLIRArithmetic arithmetic;

    public ArithmeticWriter(LLIRArithmetic arithmetic, String name){
        this.code  = "";
        this.arithmetic = arithmetic;
        this.code += generateCode(arithmetic.getLeftExpression(),name);     // left
        this.code += generateCode(arithmetic.getRightExpression(),name);    // right
        this.code += CGConst.arithmeticOperators.get(arithmetic.getOperation()) + "\n";
    }

    private String generateCode(LLIRExpression expression,String name){

        IntOperationWriter intOperationWriter = new IntOperationWriter(expression,name);
        return intOperationWriter.getCode();
    }

    public String getCode(){
        return this.code;
    }

}
