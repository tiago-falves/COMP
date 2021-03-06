package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import optimizations.OptimizationManager;
import optimizations.RegisterReducer;
import optimizations.ConstantFoldingConditional;
import optimizations.ConstantFoldingNegation;
import symbols.ConstantDescriptor;
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

        ConstantDescriptor constantDescriptor = assignment.getVariable().getVariable().getConstantDescriptor();
        if(!(OptimizationManager.constantPropagation && constantDescriptor.isConstant())) {

            String name = assignment.getVariable().getVariable().getName();
            String variableIndex = FunctionBody.getVariableIndexExists(name);
            boolean variableIndexNotFound = false;
            if(variableIndex == ""){
                variableIndexNotFound = true;
                this.code += "\taload_0\n";
            }

            type =getVariableCode(variableIndexNotFound);

            if(OptimizationManager.constantFolding && assignment.getExpression() instanceof LLIRConditional){
                LLIRConditional assignmentConditional = (LLIRConditional)assignment.getExpression();                
                ConstantFoldingConditional constantFoldingConditional = new ConstantFoldingConditional(assignmentConditional);
                assignment.setExpression(constantFoldingConditional.getConditional());
            }else if(OptimizationManager.constantFolding && assignment.getExpression() instanceof LLIRNegation){
                LLIRNegation assignmentNegation = (LLIRNegation)assignment.getExpression();
                ConstantFoldingNegation constantFoldingNegation = new ConstantFoldingNegation(assignmentNegation);
                assignment.setExpression(constantFoldingNegation.getNegation());
            }

            getAssignmentExpression();

            // get the instruction to store
            if(isArrayAccess){
                this.code += "\tiastore\n";
            }else{
                variableIndex = FunctionBody.getVariableIndexExists(name);

                if(variableIndex != ""){
                    boolean foundVariable = false;

                    this.code += CGConst.store.get(type);
                    
                    if(OptimizationManager.reducedLocals && !RegisterReducer.firstPass) {
                        String variableName = assignment.getVariable().getVariable().getName();
    
                        if(RegisterReducer.allocation.containsKey(variableName)) {
                            int register = RegisterReducer.allocation.get(variableName);
                            this.code += FunctionBody.getVariableIndexOptimized(register) + "\n";
                            foundVariable = true;
                        }   
                    }

                    if(!foundVariable)
                        this.code += variableIndex + "\n";

                    if(OptimizationManager.reducedLocals && RegisterReducer.firstPass){
                        //Adds if the variable Index already exists
                        RegisterReducer.addDef(name);
                    }
                }else{
                    this.code += CGConst.PUT_FIELD + FunctionBody.getField(name.equals("field") ? "_field" : name, type);
                    FunctionBody.incStack();
                    FunctionBody.incStack();
                }

                
            }
            FunctionBody.decStack(1);
        }
    }

    public Type getAssignmentExpression(){
        LLIRExpression expression = assignment.getExpression();
        Type type = Type.INT;

        if(expression instanceof LLIRThis){
            this.code += "\taload_0\n";
        } else if(expression instanceof LLIRInteger) {
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
        else if(expression instanceof LLIRImport){
            ImportWriter importWriter = new ImportWriter((LLIRImport)expression);
            this.code += importWriter.getCode();
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
            ArrayAccessWriter arrayAccessWriter = new ArrayAccessWriter((LLIRArrayAccess) expression,true);
            this.code += arrayAccessWriter.getCode();
            type = Type.INT_ARRAY;
        }
        else if(expression instanceof LLIRArrayLength){
            ArrayLengthWriter arrayLengthWriter = new ArrayLengthWriter((LLIRArrayLength)expression);
            this.code += arrayLengthWriter.getCode();
            type = Type.INT;
        }


        return type;
    }


    private Type getVariableCode(boolean variableIndexNotFound){

        LLIRVariableAndArray variableAndArray = assignment.getVariable();
        if(variableAndArray instanceof LLIRArrayAccess){
            ArrayAccessWriter arrayAccessWriter = new ArrayAccessWriter((LLIRArrayAccess) variableAndArray,false, variableIndexNotFound);
            this.code += arrayAccessWriter.getCode();
            isArrayAccess = true;
        }

        return assignment.getVariable().getVariable().getType();

    }

    public String getCode(){
        return this.code;
    }
    
}
