package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ArrayAccessWriter {
    private String code;
    private LLIRArrayAccess arrayAccess;

    public ArrayAccessWriter(LLIRArrayAccess arrayAccess, String name){
        this.code = "";
        this.arrayAccess = arrayAccess;
        this.code += generateArrayCode(arrayAccess.getArray());


        this.code += generateAccessCode(arrayAccess.getAccess(), name);
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
        }

        return result;
    }

    public String generateAccessCode(LLIRExpression expression, String name) {
         String result = new String();
         IntOperationWriter intOperationWriter = new IntOperationWriter(expression,name);
         result = intOperationWriter.getCode();

        return result;
    }

    public String getCode(){
        return this.code;
    }
}
