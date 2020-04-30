package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.LLIRClassVariable;
import llir.LLIRVariable;
import symbols.Type;

public class ClassVariableWriter {
    private static String NEW = "\tnew ";
    private static String DUP = "\tdup\n";
    private static String INSTRUCTION = "\tinvokespecial ";

    private String code;

    public ClassVariableWriter(LLIRClassVariable variable){
        this.code = "";

        FunctionBody.currentOperationIndex++;

        this.code += NEW + variable.getClassDescriptor().getName() + "\n";
        this.code += DUP;
        this.code += INSTRUCTION;

        //this.code += CGConst.load.get(Type.CLASS);

        FunctionBody.currentOperationIndex = 0;

       // int variableIndex = FunctionBody.getVariableIndex(variable.getVariable().getName());
        //this.code = this.code + variableIndex + "\n";
    }

    public String getCode(){
        return this.code;
    }
}
