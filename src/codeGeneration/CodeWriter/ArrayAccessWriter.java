package codeGeneration.CodeWriter;

import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ArrayAccessWriter {
    private String code;
    private LLIRArrayAccess arrayAccess;
    private boolean variableIndexNotFound;


    public ArrayAccessWriter(LLIRArrayAccess arrayAccess,boolean isLoad, boolean variableIndexNotFound){
        this.variableIndexNotFound = variableIndexNotFound;
        this.code = "";
        this.arrayAccess = arrayAccess;
        this.code += generateArrayCode(arrayAccess.getArray());
        this.code += generateAccessCode(arrayAccess.getAccess());
        if(isLoad){
            this.code += "\tiaload\n";
            FunctionBody.incStack();
        }
    }

    public ArrayAccessWriter(LLIRArrayAccess arrayAccess,boolean isLoad){
        this(arrayAccess, isLoad, false);
    }        

    public String generateArrayCode(LLIRExpression expression) {
        String result = new String();
        if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            result += methodCallWriter.getCode();
        }
        else if (expression instanceof LLIRVariable) {
            VariableWriter arrayWriter = new VariableWriter((LLIRVariable) expression, this.variableIndexNotFound);
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
