package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class AssignmentWriter {

    private String code;

    public AssignmentWriter(LLIRAssignment assignment) {


        this.code  = "";

        String name = assignment.getVariable().getVariable().getName();
        LLIRExpression expression = assignment.getExpression();
        Type type = Type.INT;

        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression,name);
            this.code += integerWriter.getCode();
            type = Type.INT;
        }
        else if (expression instanceof LLIRBoolean) {

            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean) expression,name);
            this.code += booleanWriter.getCode();
            type = Type.BOOLEAN;

        }
        else if(expression instanceof LLIRVariable) {
            VariableWriter variableWriter = new VariableWriter((LLIRVariable) expression);
            this.code += variableWriter.getCode();
        }

        else if (expression instanceof LLIRArithmetic) {
            ArithmeticTransformer transformer = new ArithmeticTransformer((LLIRArithmetic) expression);
            LLIRArithmetic transformed = transformer.transform();

            ArithmeticWriter arithmeticWriter = new ArithmeticWriter(transformed,name);
            this.code += arithmeticWriter.getCode();
        }

        else if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            this.code += methodCallWriter.getCode();
        }
        else if (expression instanceof LLIRClassVariable) {
            ClassVariableWriter classVariableWriter = new ClassVariableWriter((LLIRClassVariable) expression);
            this.code += classVariableWriter.getCode();
            type = Type.CLASS;
        }

        // get the instruction to store
        this.code += CGConst.store.get(type);
        FunctionBody.currentOperationIndex = 0;

        // assign to the correct variable
        int variableIndex = FunctionBody.getVariableIndex(name);
        this.code = this.code + variableIndex + "\n";

    }

    public String getCode(){
        return this.code;
    }
    
}
