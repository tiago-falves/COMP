package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ArithmeticWriter {
    private String code;
    private LLIRArithmetic arithmetic;

    public ArithmeticWriter(LLIRArithmetic arithmetic){
        this.code  = "";
        this.arithmetic = arithmetic;
        this.code += generateCode(arithmetic.getLeftExpression());     // left
        this.code += generateCode(arithmetic.getRightExpression());    // right
        this.code += CGConst.arithmeticOperators.get(arithmetic.getOperation()) + "\n";
    }

    private String generateCode(LLIRExpression expression){

        IntOperationWriter intOperationWriter = new IntOperationWriter(expression);
        return intOperationWriter.getCode();
    }

    public String getCode(){
        return this.code;
    }

}
