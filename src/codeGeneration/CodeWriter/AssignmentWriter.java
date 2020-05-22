package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.NamedTypeDescriptor;
import symbols.Type;

public class AssignmentWriter {

    private String code;
    private LLIRAssignment assignment;
    private boolean isArrayAccess;
    private  Type type;

    public AssignmentWriter(LLIRAssignment assignment) {

        isArrayAccess = false;
        this.code  = "";
        this.assignment = assignment;

        String name = assignment.getVariable().getVariable().getName();

        getVariableCode();

        type = getAssignmentExpression();

        // get the instruction to store
        if(isArrayAccess){
            this.code += "\tiastore\n";
        }else{

            this.code += CGConst.store.get(type);
            // assign to the correct variable
            String variableIndex = FunctionBody.getVariableIndexString(name);
            this.code = this.code + variableIndex + "\n";
        }
    }

    public Type getAssignmentExpression(){
        LLIRExpression expression = assignment.getExpression();
        Type type = Type.INT;

        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression);
            this.code += integerWriter.getCode();
            type = Type.INT;
        }
        else if (expression instanceof LLIRBoolean) {

            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean) expression);
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

            ArithmeticWriter arithmeticWriter = new ArithmeticWriter(transformed);
            this.code += arithmeticWriter.getCode();
        }

        else if(expression instanceof LLIRConditional) {
            ConditionalWriter conditionalWriter = new ConditionalWriter((LLIRConditional) expression);
            this.code += conditionalWriter.getCode();
        }

        else if(expression instanceof LLIRNegation){
            NegationWriter negationWriter = new NegationWriter((LLIRNegation)expression);
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
            ArrayAccessWriter arrayAccessWriter = new ArrayAccessWriter((LLIRArrayAccess) expression);
            this.code += arrayAccessWriter.getCode();
            type = Type.INT_ARRAY;

        }

        return type;
    }


    private void getVariableCode(){

        LLIRVariableAndArray variableAndArray = assignment.getVariable();
        if(variableAndArray instanceof LLIRArrayAccess){
            ArrayAccessWriter arrayAccessWriter = new ArrayAccessWriter((LLIRArrayAccess) variableAndArray);
            this.code += arrayAccessWriter.getCode();
            isArrayAccess = true;

        }

    }

    public String getCode(){
        return this.code;
    }
    
}
