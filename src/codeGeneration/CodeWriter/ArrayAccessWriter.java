package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ArrayAccessWriter {
    private String code;
    private LLIRArrayAccess arrayAccess;

    public ArrayAccessWriter(LLIRArrayAccess arrayAccess){
        this.code = "";
        this.arrayAccess = arrayAccess;
        this.code += generateArrayCode(arrayAccess.getArray());
        System.out.println(arrayAccess.getAccess());
        this.code += generateAccessCode(arrayAccess.getAccess());
    }

    public String generateArrayCode(LLIRExpression expression) {
        String result = new String();
        if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            result += methodCallWriter.getCode();
        }
        else if (expression instanceof LLIRVariable) {
            System.out.println("GANDA ZAS");
            VariableWriter arrayWriter = new VariableWriter((LLIRVariable) expression);
            result += arrayWriter.getCode();
        }else{
            System.out.println(expression);
        }

        return result;
    }

    public String generateAccessCode(LLIRExpression expression) {
         String result = new String();
         IntOperationWriter intOperationWriter = new IntOperationWriter(expression);
         result = intOperationWriter.getCode();

        return result;
    }

    public String getCode(){
        return this.code;
    }
}
