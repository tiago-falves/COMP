package codeGeneration.CodeWriter;

import llir.*;

public class WhileWriter extends BlockStatementWriter {
    private String code;
    private LLIRWhileBlock block;

    private static int whileNumber = 0;

    public WhileWriter(LLIRWhileBlock block, String name){
        
        int localWhileNumber = whileNumber;
        whileNumber++;
        this.code  = "while_" + localWhileNumber + ":\n";
        this.block = block;
        
        this.code += "while_" + localWhileNumber + ":" + "\n";

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
