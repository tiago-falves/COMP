package codeGeneration.CodeWriter;

import java.util.List;

import llir.*;

public abstract class BlockStatementWriter {

    protected String generateNodesCode(List<LLIRNode> nodes, String name){
        String result = new String();
        for(int i = 0; i < nodes.size(); i++){
            result += generateCode(nodes.get(i), name);
        }
        return result;
    }

    protected String generateCode(LLIRNode node,String name){

        String result = new String();
        if(node instanceof LLIRAssignment) {
            AssignmentWriter assignmentWriter = new AssignmentWriter((LLIRAssignment) node);
            result += assignmentWriter.getCode();
        }
        else if (node instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) node);
            result += methodCallWriter.getCode();
        }
        else if (node instanceof LLIRImport){
            ImportWriter importWriter = new ImportWriter((LLIRImport) node);
            result += importWriter.getCode();
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
            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean) node);
            result += booleanWriter.getCode();
        }
        else if (node instanceof LLIRVariable){
            VariableWriter variableWriter = new VariableWriter((LLIRVariable) node);
            result += variableWriter.getCode();
        }

        return result;
    }
}
