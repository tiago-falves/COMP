package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.LLIRClassVariableInstantiation;
import llir.LLIRExpression;
import symbols.Type;

public class ClassVariableInstantiationWriter {
    private static String NEW = "\tnew ";
    private static String DUP = "\tdup\n";
    private static String INSTRUCTION = "\tinvokespecial ";

    private String code;

    public ClassVariableInstantiationWriter(LLIRClassVariableInstantiation variable, boolean onMethodCall){
        this.code = "";

        if (!onMethodCall) {
            FunctionBody.incStack();
            FunctionBody.incStack();
        }

        this.code += NEW + variable.getName() + "\n";
        this.code += DUP;

        if(variable.getParameters().size() > 0){
            this.code += this.generateParametersCode(variable);
        }

        this.code += INSTRUCTION + variable.getName() + "/<init>(";
        
        if(variable.getParameters().size() > 0){
            this.code += this.generateParametersType(variable);
        }

        this.code += ")V\n";
    }

    public ClassVariableInstantiationWriter(LLIRClassVariableInstantiation variable) {
        this(variable, false);
    }
    
    public String generateParametersCode(LLIRClassVariableInstantiation variable){
        String s = new String();

        for (LLIRExpression expression : variable.getParameters()) {
            ExpressionWriter expressionWriter = new ExpressionWriter(expression);
            s += expressionWriter.getCode();
        }

        return s;
    }

    public String generateParametersType(LLIRClassVariableInstantiation variable){
        String s = new String();

        for (Type type : variable.getParametersTypes()) {
            s += CGConst.types.get(type);
        }

        return s;
    }


    public String getCode(){
        return this.code;
    }
}
