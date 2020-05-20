package codeGeneration.CodeWriter;

import codeGeneration.FunctionBody;
import llir.LLIRArrayInstantiation;
import llir.LLIRClassVariableInstantiation;
import llir.LLIRExpression;

public class ArrayInstantiationWriter {
    private static String NEWARRAY = "\tnewarray int\n";
    private static String DUP = "\tdup\n";
    private static String INSTRUCTION = "\tinvokespecial ";

    private String code;

    public ArrayInstantiationWriter(LLIRArrayInstantiation variable){
        this.code = "";

        IntOperationWriter integerWriter = new IntOperationWriter(variable.getSize(),"");
        this.code += integerWriter.getCode();
        this.code += NEWARRAY;

        FunctionBody.currentOperationIndex++;

        //this.code += NEW + variable.getClassDescriptor().getName() + "\n";
        //this.code += DUP;
        //this.code += INSTRUCTION + variable.getClassDescriptor().getName() + "/<init>()V\n";


        //this.code += CGConst.load.get(Type.CLASS);

        FunctionBody.currentOperationIndex = 0;

       // int variableIndex = FunctionBody.getVariableIndex(variable.getVariable().getName());
        //this.code = this.code + variableIndex + "\n";
    }

    public String getCode(){
        return this.code;
    }
}
