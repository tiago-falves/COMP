package codeGeneration.CodeWriter;

import java.util.List;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class WhileWriter {
    private String code;
    /*private LLIRWhileBlock block;

    private static int whileNumber = 0;
*/
    public WhileWriter(LLIRWhileBlock block, String name){
  /*      
        int localWhileNumber = whileNumber;
        whileNumber++;

        this.code  = "";
        this.block = block;
        
        this.code += "while_" + localWhileNumber + ":" + "\n";

        this.code += generateCode(block.getExpression(), name);

        this.code += "\t" + "ifeq endWhile_" + localWhileNumber + "\n";

        this.code += generateNodesCode(block.getNodes(), name);

        this.code += "\t" + "goto while_" + localWhileNumber + "\n";

        this.code += "endWhile_" + localWhileNumber + ":" + "\n";

        localWhileNumber++;
    */}
/*
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
            WhileWriter whileWriter = new WhileWriter((LLIRWhileBlock) node, name);
            result += whileWriter.getCode();
        }
        else if (node instanceof LLIRBoolean){
            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean) node, name);
            result += booleanWriter.getCode();
        }

        return result;
    }
*/
    public String getCode(){
        return this.code;
    }//*/

}
