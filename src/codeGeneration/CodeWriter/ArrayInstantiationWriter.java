package codeGeneration.CodeWriter;

import codeGeneration.FunctionBody;
import llir.LLIRArrayInstantiation;
import llir.LLIRClassVariableInstantiation;
import llir.LLIRExpression;

public class ArrayInstantiationWriter {
    private static String NEWARRAY = "\tnewarray int\n";

    private String code;

    public ArrayInstantiationWriter(LLIRArrayInstantiation variable){
        this.code = "";

        IntOperationWriter integerWriter = new IntOperationWriter(variable.getSize());
        this.code += integerWriter.getCode();
        this.code += NEWARRAY;

        FunctionBody.incStack();
    }

    public String getCode(){
        return this.code;
    }
}
