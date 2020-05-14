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
        else if (node instanceof LLIRConditional){
            ConditionalWriter conditionalWriter = new ConditionalWriter((LLIRConditional)node, name);
            result += conditionalWriter.getCode();
        }
        else if (node instanceof LLIRNegation){
            NegationWriter negationWriter = new NegationWriter((LLIRNegation)node, name);
            result += negationWriter.getCode();
        }
        else if (node instanceof LLIRBoolean){
            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean) node, name);
            result += booleanWriter.getCode();
        }

        return result;
    }

    public String getCode(){
        return this.code;
    }

}
