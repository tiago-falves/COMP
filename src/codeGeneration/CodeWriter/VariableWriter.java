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

        FunctionBody.incStack();

        String variableIndex = FunctionBody.getVariableIndexExists(variable.getVariable().getName());
        if(variableIndex == "") {
            this.code += "\taload_0\n";
            this.code += CGConst.GET_FIELD + FunctionBody.getField(variable.getVariable().getName().equals("field") ? "_field" : variable.getVariable().getName(),variable.getVariable().getType());
        }
        else{
            this.code += CGConst.load.get(variable.getVariable().getType());
            this.code +=variableIndex + "\n";
        } 


        

        
    }

    public String getCode(){
        return this.code;
    }
}
