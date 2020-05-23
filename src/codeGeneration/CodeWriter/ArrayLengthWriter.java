package codeGeneration.CodeWriter;

import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ArrayLengthWriter {
    private String code;
    private LLIRArrayLength arrayLength;

    public ArrayLengthWriter(LLIRArrayLength arrayLength){
        this.code = "";
        this.arrayLength = arrayLength;
        this.code += generateArrayCode(arrayLength.getArray());
        this.code += "\tarraylength\n";
    }

    public String generateArrayCode(LLIRExpression expression) {
        String result = new String();
        if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            result += methodCallWriter.getCode();
        }
        else if (expression instanceof LLIRVariable) {
            VariableWriter arrayWriter = new VariableWriter((LLIRVariable) expression);
            result += arrayWriter.getCode();
        }else{
            System.out.println(expression);
        }

        return result;
    }

    public String getCode(){
        return this.code;
    }
}
