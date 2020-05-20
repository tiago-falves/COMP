package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class AssignmentWriter {

    private String code;

    public AssignmentWriter(LLIRAssignment assignment) {


        boolean isArrayAccess = false;
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

        else if(expression instanceof LLIRConditional) {
            ConditionalWriter conditionalWriter = new ConditionalWriter((LLIRConditional) expression, name);
            this.code += conditionalWriter.getCode(); 
        }

        else if(expression instanceof LLIRNegation){
            NegationWriter negationWriter = new NegationWriter((LLIRNegation)expression, name);
            this.code += negationWriter.getCode();
        }

        else if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            this.code += methodCallWriter.getCode();
        }
        else if (expression instanceof LLIRClassVariableInstantiation) {
            ClassVariableInstantiationWriter classVariableWriter = new ClassVariableInstantiationWriter((LLIRClassVariableInstantiation) expression);
            this.code += classVariableWriter.getCode();
            type = Type.CLASS;
        } else if (expression instanceof LLIRArrayInstantiation) {
            ArrayInstantiationWriter arrayInstantiationWriter = new ArrayInstantiationWriter((LLIRArrayInstantiation) expression);
            this.code += arrayInstantiationWriter.getCode();
            type = Type.INT_ARRAY;
        }

        else if (expression instanceof LLIRArrayAccess) {
            ArrayAccessWriter arrayAccessWriter = new ArrayAccessWriter((LLIRArrayAccess) expression,name);
            this.code += arrayAccessWriter.getCode();
            type = Type.INT_ARRAY;
            isArrayAccess = true;
        }

        // get the instruction to store
        if(isArrayAccess){
            this.code += "\tiastore\n";
        }else{

            this.code += CGConst.store.get(type);
            // assign to the correct variable
            String variableIndex = FunctionBody.getVariableIndexString(name);
            this.code = this.code + variableIndex + "\n";
        }
        FunctionBody.currentOperationIndex = 0;


    }

    public String getCode(){
        return this.code;
    }
    
}
