package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.LLIRVariable;
import symbols.Type;

public class ArrayWriter {
    private String code;

    public ArrayWriter(LLIRVariable array){
        this.code = "";

        this.code += "\tiaload\n";
        FunctionBody.incStack();
    }

    public String getCode(){
        return this.code;
    }
}

