package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import symbols.Type;

public class ArrayWriter {
    private String code;

    public ArrayWriter(LLIRArray array){
        this.code = "";

        //TODO check this
        FunctionBody.currentOperationIndex++;

        this.code += CGConst.load.get(Type.INT_ARRAy);

        //TODO check this
        FunctionBody.currentOperationIndex = 0;

        String arrayIndex = FunctionBody.getVariableIndexString(array.getArray().getName());
        this.code = this.code + variableIndex + "\n";
    }

    public String getCode(){
        return this.code;
    }
}
