package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.LLIRInteger;
import llir.LLIRBoolean;
import llir.LLIRVariable;
import optimizations.OptimizationManager;
import optimizations.OptimizationsR;
import symbols.ConstantDescriptor;
import symbols.ConstantInt;
import symbols.ConstantBoolean;

public class VariableWriter {
    private String code;
    private boolean variableIndexNotFound;
    private boolean isConstant;

    public VariableWriter(LLIRVariable variable, boolean variableIndexNotFound){
        this.variableIndexNotFound = variableIndexNotFound;
        this.code = "";

        ConstantDescriptor constantDescriptor = variable.getVariable().getConstantDescriptor();
        this.isConstant = constantDescriptor.isConstant();

        if(OptimizationManager.constantPropagation && isConstant) {

            if(constantDescriptor instanceof ConstantInt) {
                ConstantInt constantInt = (ConstantInt) constantDescriptor;
                IntegerWriter writer = new IntegerWriter(new LLIRInteger(constantInt.getValue()));
                this.code += writer.getCode();
            } else {
                ConstantBoolean constantBoolean = (ConstantBoolean) constantDescriptor;
                BooleanWriter writer = new BooleanWriter(new LLIRBoolean(constantBoolean.getValue()));
                this.code += writer.getCode();
            }
            
        }
        else {
            FunctionBody.incStack();

            String variableIndex = FunctionBody.getVariableIndexExists(variable.getVariable().getName());

            if(variableIndex == "") {
                if(!this.variableIndexNotFound){
                    this.code += "\taload_0\n";
                }
                this.code += CGConst.GET_FIELD + FunctionBody.getField(variable.getVariable().getName().equals("field") ? "_field" : variable.getVariable().getName(),variable.getVariable().getType());
            }
            else{
                this.code += CGConst.load.get(variable.getVariable().getType());
                this.code +=variableIndex + "\n";

                if(OptimizationManager.reducedLocals && OptimizationsR.firstPass)
                    OptimizationsR.addUse(variable.getVariable().getName());
            } 
        }
    }

    public VariableWriter(LLIRVariable variable){
        this(variable, false);
    }

    public String getCode(){
        return this.code;
    }

}
