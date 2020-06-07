package codeGeneration.CodeWriter;

import llir.*;
import optimizations.ConstantFoldingConditional;
import optimizations.ConstantFoldingNegation;
import optimizations.OptimizationManager;

public class WhileWriter extends BlockStatementWriter {
    private String code;
    private LLIRWhileBlock block;

    private static int whileNumber = 0;

    public WhileWriter(LLIRWhileBlock block, String name){
        
        int localWhileNumber = whileNumber;
        whileNumber++;
        this.block = block;

        this.code = "";
        
        this.code += "while_" + localWhileNumber + ":" + "\n";
        if(OptimizationManager.constantFolding && block.getExpression() instanceof LLIRConditional){
            LLIRConditional assignmentConditional = (LLIRConditional)block.getExpression();
            ConstantFoldingConditional constantFoldingConditional = new ConstantFoldingConditional(assignmentConditional);
            block.setExpression(constantFoldingConditional.getConditional());
        }else if(OptimizationManager.constantFolding && block.getExpression() instanceof LLIRNegation){
            LLIRNegation assignmentNegation = (LLIRNegation)block.getExpression();
            ConstantFoldingNegation constantFoldingNegation = new ConstantFoldingNegation(assignmentNegation);
            block.setExpression(constantFoldingNegation.getNegation());
        }
        this.code += generateCode(block.getExpression(), name);

        this.code += "\t" + "ifeq endWhile_" + localWhileNumber + "\n";

        this.code += generateNodesCode(block.getNodes(), name);

        this.code += "\t" + "goto while_" + localWhileNumber + "\n";

        this.code += "endWhile_" + localWhileNumber + ":" + "\n";

        localWhileNumber++;
    }

    public String getCode(){
        return this.code;
    }

}
