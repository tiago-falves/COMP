package codeGeneration.CodeWriter;

import llir.*;
import optimizations.ConstantFoldingConditional;
import optimizations.ConstantFoldingNegation;
import optimizations.OptimizationManager;

public class IfElseWriter extends BlockStatementWriter{
    private String code;
    private LLIRIfElseBlock block;

    private static int ifNumber = 0;

    public IfElseWriter(LLIRIfElseBlock block, String name){
        super();

        int localIfNumber = ifNumber;
        ifNumber++;

        this.code  = "";
        this.block = block;
        
        //If expression
        if(OptimizationManager.constantFolding && block.getExpression() instanceof LLIRConditional){
            LLIRConditional assignmentConditional = (LLIRConditional)block.getExpression();
            ConstantFoldingConditional constantFoldingConditional = new ConstantFoldingConditional(assignmentConditional);
            block.setExpression(constantFoldingConditional.getConditional());
        }else if(OptimizationManager.constantFolding && block.getExpression() instanceof LLIRNegation){
            LLIRNegation assignmentNegation = (LLIRNegation)block.getExpression();
            ConstantFoldingNegation constantFoldingNegation = new ConstantFoldingNegation(assignmentNegation);
            block.setExpression(constantFoldingNegation.getNegation());
        }
        this.code += generateCode(block.getExpression(),name);

        this.code += "\t" + "ifeq else_" + localIfNumber + "\n";

        this.code += generateNodesCode(block.getIfNodes(), name);
        
        this.code += "\t" + "goto endIf_" + localIfNumber + "\n";

        //Else
        this.code += "else_" + localIfNumber + ":" + "\n";

        this.code += generateNodesCode(block.getElseNodes(), name);

        this.code += "endIf_" + localIfNumber + ":" + "\n";

        localIfNumber++;
    }

    public String getCode(){
        return this.code;
    }

}
