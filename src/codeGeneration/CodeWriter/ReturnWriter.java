package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ReturnWriter {

    private String code;
    private static String RETURN = "\treturn\n";

    public ReturnWriter(LLIRReturn returnLLIR) {
        this.code  = "";
        LLIRExpression expression = returnLLIR.getExpression();
        ExpressionWriter expressionWriter = new ExpressionWriter(expression,"NAME");
        this.code+= expressionWriter.getCode();
        this.code+=RETURN;
        //this.code+= CGConst.returnTypes.get(returnLLIR.getReturnType());
    }

    public String getCode(){
        return this.code;
    }
    
}
