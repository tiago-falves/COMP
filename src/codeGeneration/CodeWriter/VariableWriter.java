package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.LLIRInteger;
import llir.LLIRVariable;
import symbols.Type;

public class VariableWriter {
    private String code;

    public VariableWriter(LLIRVariable variable){
        this.code = "";

        FunctionBody.currentOperationIndex++;

        this.code += CGConst.load.get(Type.INT);

        FunctionBody.currentOperationIndex = 0;

        int variableIndex = FunctionBody.getVariableIndex(variable.getVariable().getName());
        this.code = this.code + variableIndex + "\n";
    }

    public String getCode(){
        return this.code;
    }
}
