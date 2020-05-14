package codeGeneration.CodeWriter;

import llir.*;

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
