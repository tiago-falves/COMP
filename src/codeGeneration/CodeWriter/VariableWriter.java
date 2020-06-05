package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.LLIRInteger;
import llir.LLIRVariable;
import symbols.Type;

public class VariableWriter {
    private String code;
    private boolean variableIndexNotFound;

    public VariableWriter(LLIRVariable variable, boolean variableIndexNotFound){
        this.variableIndexNotFound = variableIndexNotFound;
        this.code = "";

        FunctionBody.incStack();

        String variableIndex = FunctionBody.getVariableIndexExists(variable.getVariable().getName());
        if(variableIndex == "") {
            if(!this.variableIndexNotFound){
                this.code += "\taload_0\n";
            }
            this.code += CGConst.GET_FIELD + FunctionBody.getField(variable.getVariable().getName().equals("field") ? "_field" : variable.getVariable().getName(),variable.getVariable().getType());
        }
        else{
            this.code += CGConst.load.get(variable.getVariable().getType());
            this.code +=variableIndex + "\n";
        } 
    }

    public VariableWriter(LLIRVariable variable){
        this(variable, false);
    }

    public String getCode(){
        return this.code;
    }
}
