package codeGeneration.CodeWriter;

import java.util.List;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class IfElseWriter {
    private String code;
    private LLIRIfElseBlock block;

    private static int ifNumber = 0;

    public IfElseWriter(LLIRIfElseBlock block, String name){
        this.code  = "";
        this.block = block;
        
        //If expression
        this.code += generateCode(block.getExpression(),name);

        this.code += "\t" + "ifeq else_" + ifNumber + "\n";

        this.code += generateNodesCode(block.getIfNodes(), name);
        
        this.code += "\t" + "goto endIf_" + ifNumber + "\n";

        //Else
        this.code += "else_" + ifNumber + ":" + "\n";

        this.code += generateNodesCode(block.getElseNodes(), name);

        this.code += "endIf_" + ifNumber + ":" + "\n";

        ifNumber++;
    }

    private String generateNodesCode(List<LLIRNode> nodes, String name){
        String result = new String();
        for(int i = 0; i < nodes.size(); i++){
            result += generateCode(nodes.get(i), name);
        }
        return result;
    }

    private String generateCode(LLIRNode node,String name){

        String result = new String();
        if(node instanceof LLIRAssignment) {
            AssignmentWriter assignmentWriter = new AssignmentWriter((LLIRAssignment) node);
            result += assignmentWriter.getCode();
        }
        else if (node instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) node);
            result += methodCallWriter.getCode();
        }
        else if (node instanceof LLIRIfElseBlock) {
            IfElseWriter ifElseWriter = new IfElseWriter((LLIRIfElseBlock) node,name);
            result += ifElseWriter.getCode();
        }
        else if (node instanceof LLIRWhileBlock){
            WhileWriter whileWriter = new WhileWriter((LLIRWhileBlock) node, name)
            result += whileWriter.getCode();
        }

        return result;
    }

    public String getCode(){
        return this.code;
    }

}
