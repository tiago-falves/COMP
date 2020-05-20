package codeGeneration.CodeWriter;

import codeGeneration.FunctionBody;
import llir.LLIRClassVariableInstantiation;

public class ClassVariableInstantiationWriter {
    private static String NEW = "\tnew ";
    private static String DUP = "\tdup\n";
    private static String INSTRUCTION = "\tinvokespecial ";

    private String code;

    public ClassVariableInstantiationWriter(LLIRClassVariableInstantiation variable){
        this.code = "";

        FunctionBody.currentOperationIndex++;

        this.code += NEW + variable.getClassDescriptor().getName() + "\n";
        this.code += DUP;
        this.code += INSTRUCTION + variable.getClassDescriptor().getName() + "/<init>()V\n";

        FunctionBody.currentOperationIndex = 0;


    }

    public String getCode(){
        return this.code;
    }
}
