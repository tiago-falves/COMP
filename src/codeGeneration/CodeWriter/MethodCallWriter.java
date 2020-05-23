package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import codeGeneration.FunctionParameters;
import llir.LLIRExpression;
import llir.LLIRMethodCall;
import symbols.Type;

import java.util.List;

public class MethodCallWriter {
    private static String LOAD = "\taload";
    private static String INSTRUCTION = "\tinvokevirtual ";
    private String code = "";

    public MethodCallWriter(LLIRMethodCall methodCall){

        if(methodCall.getClassVariableInstantiation() != null){
            ClassVariableInstantiationWriter classVariableInstantiationWriter = new ClassVariableInstantiationWriter(methodCall.getClassVariableInstantiation());
            this.code += classVariableInstantiationWriter.getCode();
            System.out.println(methodCall.getClassVariableInstantiation().getClassDescriptor().getName());
        }
        else if (methodCall.getClassName() == "") this.code = LOAD + "_0\n";
        else{
            String variableIndex = FunctionBody.getVariableIndexString(methodCall.getClassName());
            this.code = LOAD + variableIndex + "\n";
        }
        FunctionBody.incStack();

        this.code += getParameters(methodCall);
        this.code += INSTRUCTION;

        String arguments = FunctionParameters.getParametersTypes(methodCall.getParametersTable());
        //TODO Aceder ao nome da classe sem fazer isto do Upper Case
        if (methodCall.getClassName()!="") this.code+= methodCall.getClassName().substring(0, 1).toUpperCase() + methodCall.getClassName().substring(1) + "/";
        if (methodCall.getClassVariableInstantiation()!=null) this.code+= methodCall.getClassVariableInstantiation().getClassDescriptor().getName()+ "/";


        this.code += methodCall.getMethodName() + "(" + arguments + ")"+ CGConst.types.get(methodCall.getReturnType()) + "\n";
        FunctionBody.decStack(methodCall.getParametersTable().getSize()-(methodCall.getReturnType() == Type.VOID ? 0 : 1));
    }

    public String getCode(){
        return this.code;
    }

    private String getParameters(LLIRMethodCall methodCall){
        String result = "";

        List<LLIRExpression> parameters = methodCall.getParametersExpressions();
        for (LLIRExpression expression : parameters){
            ExpressionWriter expressionWriter = new ExpressionWriter(expression);
            result += expressionWriter.getCode();
        }

        return result;
    }
}
