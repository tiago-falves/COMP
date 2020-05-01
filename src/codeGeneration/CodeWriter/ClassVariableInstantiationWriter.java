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


        //this.code += CGConst.load.get(Type.CLASS);

        FunctionBody.currentOperationIndex = 0;

       // int variableIndex = FunctionBody.getVariableIndex(variable.getVariable().getName());
        //this.code = this.code + variableIndex + "\n";
    }

    public String getCode(){
        return this.code;
    }
}
