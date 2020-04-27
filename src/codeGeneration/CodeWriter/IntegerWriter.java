package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.LLIRInteger;
import symbols.Type;

public class IntegerWriter {
    private String code;

    public IntegerWriter(LLIRInteger integer,String name){
        String generatedCode = new String();

        int value = integer.getValue();
        if(value <= 5) {
            generatedCode += "\ticonst_" + value + "\n";
        } else {
            generatedCode += "\tbipush\t" + value + "\n";
        }
        FunctionBody.currentOperationIndex++;

        generatedCode += CGConst.store.get(Type.INT);
        FunctionBody.currentOperationIndex = 0;

        int variableIndex = FunctionBody.getVariableIndex(name);
        this.code = generatedCode + variableIndex + "\n";
    }

    public String getCode(){
        return this.code;
    }
}
