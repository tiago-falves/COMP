package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.LLIRInteger;
import symbols.Type;

public class IntegerWriter {
    private String code;

    public IntegerWriter(LLIRInteger integer){
        String generatedCode = new String();

        int value = integer.getValue();
        if(value <= 5) {
            generatedCode += "\ticonst_" + value + "\n";
        } else if(value <= 127){
            generatedCode += "\tbipush\t" + value + "\n";
        } else if(value <= 32767){
            generatedCode += "\tsipush\t" + value + "\n";
        } else{
            generatedCode += "\tldc\t" + value + "\n";
        }
        FunctionBody.incStack();

        this.code = generatedCode;
    }

    public String getCode(){
        return this.code;
    }
}
