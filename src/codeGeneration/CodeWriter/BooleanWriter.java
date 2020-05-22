package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.LLIRBoolean;
import llir.LLIRInteger;
import symbols.Type;

import static codeGeneration.FunctionBody.*;

public class BooleanWriter {
    private String code;

    public BooleanWriter(LLIRBoolean llirBoolean){
        this.code = new String();

        boolean value = llirBoolean.getValue();

        if(value)
            this.code += CGConst.TRUE_VALUE + "\n";
        else {
            this.code += CGConst.FALSE_VALUE + "\n";
        }
        FunctionBody.incStack();
    }

    public String getCode(){
        return this.code;
    }
}
