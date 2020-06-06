package codeGeneration.CodeWriter;

import java.util.List;

import codeGeneration.FunctionBody;
import llir.*;
import optimizations.OptimizationManager;
import optimizations.OptimizationsR;

public abstract class BlockStatementWriter {

    protected String generateNodesCode(List<LLIRNode> nodes, String name){

        int expressionNumber = 0;
        if(OptimizationManager.reducedLocals)
            expressionNumber = OptimizationsR.currentLine;


        String result = new String();
        for(int i = 0; i < nodes.size(); i++){

            // Calculate liveness on first pass
            if(OptimizationManager.reducedLocals && OptimizationsR.firstPass){
                OptimizationsR.incrementLine();
                OptimizationsR.addPredSucc();
            }
            result += generateCode(nodes.get(i), name);
        }

        // Continue calculating liveness on first pass
        if(OptimizationManager.reducedLocals && OptimizationsR.firstPass) {
            OptimizationsR.addBlockPredSuccExpression(this,expressionNumber);
            return "";
        }

        return result;
    }

    protected String generateCode(LLIRNode node,String name){

        String result = new String();
        if(node instanceof LLIRAssignment) {
            FunctionBody.resetStack();
            AssignmentWriter assignmentWriter = new AssignmentWriter((LLIRAssignment) node);
            result += assignmentWriter.getCode();
        }
        else if (node instanceof LLIRMethodCall) {
            FunctionBody.resetStack();
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) node);
            result += methodCallWriter.getCode();
        }
        else if (node instanceof LLIRImport){
            FunctionBody.resetStack();
            ImportWriter importWriter = new ImportWriter((LLIRImport) node);
            result += importWriter.getCode();
        }
        else if (node instanceof LLIRIfElseBlock) {
            FunctionBody.resetStack();
            IfElseWriter ifElseWriter = new IfElseWriter((LLIRIfElseBlock) node,name);
            result += ifElseWriter.getCode();
        }
        else if (node instanceof LLIRWhileBlock){
            FunctionBody.resetStack();
            WhileWriter whileWriter = new WhileWriter((LLIRWhileBlock) node, name);
            result += whileWriter.getCode();
        }
        else if (node instanceof LLIRConditional){
            ConditionalWriter conditionalWriter = new ConditionalWriter((LLIRConditional)node);
            result += conditionalWriter.getCode();
        }//TODO Acho que estes nao podem estar aqui
        else if (node instanceof LLIRNegation){
            NegationWriter negationWriter = new NegationWriter((LLIRNegation)node);
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
