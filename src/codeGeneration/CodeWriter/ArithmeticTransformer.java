package codeGeneration.CodeWriter;

import java.util.List;
import java.util.ArrayList;

import llir.LLIRArithmetic;
import llir.LLIRExpression;
import llir.LLIRInteger;
import llir.ArithmeticOperation;

import optimizations.*;

public class ArithmeticTransformer {
    LLIRArithmetic originalArithmetic;

    public ArithmeticTransformer(LLIRArithmetic originalArithmetic) {
        this.originalArithmetic = originalArithmetic;
    }

    public LLIRArithmetic transform() {
        List<LLIRExpression> expressions = new ArrayList<>();
        List<ArithmeticOperation> operators = new ArrayList<>();

        // flatten arithmetic
        expressions.add(originalArithmetic.getLeftExpression());
        operators.add(originalArithmetic.getOperation());
        while(true) {
            LLIRExpression rightExpression = originalArithmetic.getRightExpression();
            if(rightExpression instanceof LLIRArithmetic) {
                LLIRArithmetic rightArithmetic = (LLIRArithmetic) rightExpression;
                expressions.add(rightArithmetic.getLeftExpression());
                operators.add(rightArithmetic.getOperation());
                originalArithmetic = rightArithmetic;

            }
            else break;
        }

        expressions.add(originalArithmetic.getRightExpression());
       
        LLIRArithmetic result = (LLIRArithmetic) recursiveTransform(expressions, operators, 1);

        if(OptimizationManager.constantFolding){
            LLIRArithmetic tmp_result = result;
            //try{
                LLIRArithmetic actual = result;
                ConstantFolding constantFolding = new ConstantFolding(actual);
                LLIRExpression expr = constantFolding.getArithmetic();

                if(expr instanceof LLIRArithmetic){
                    result = (LLIRArithmetic)expr;
                }else{
                    result = new LLIRArithmetic(ArithmeticOperation.SUM, expr, new LLIRInteger(0));
                }
            /* }catch(Exception e){
                System.out.println(e.getMessage());
                result = tmp_result;
            }*/
        }
        
        return result;
    }


    public LLIRExpression recursiveTransform(List<LLIRExpression> expressions, List<ArithmeticOperation> operators, int priorityLevel) {
        
        while(priorityLevel < 4) {
            for(int i = 0; i < operators.size(); i++) {
                if(priorityLevel == 2 && (operators.get(i) == ArithmeticOperation.SUM || operators.get(i) == ArithmeticOperation.SUBTRACTION)) {
                    List<ArithmeticOperation> leftOperations = sliceOperators(operators, 0, i);
                    List<ArithmeticOperation> rightOperations = sliceOperators(operators, i+1, operators.size());

                    List<LLIRExpression> leftExpressions = sliceExpressions(expressions, 0, i+1);
                    List<LLIRExpression> rightExpressions = sliceExpressions(expressions, i+1, expressions.size());

                    ArithmeticOperation op = operators.get(i);
                    while(rightOperations.size() > 0 && (rightOperations.get(0) == ArithmeticOperation.SUM || rightOperations.get(0) == ArithmeticOperation.SUBTRACTION)) {
                        leftOperations.add(op);
                        op = rightOperations.get(0);
                        rightOperations.remove(0);

                        leftExpressions.add(rightExpressions.get(0));
                        rightExpressions.remove(0);

                        i++;
                    }

                    // build tree left - right in order of descending depth
                    if(leftExpressions.size() >= rightExpressions.size()) {
                        return new LLIRArithmetic(
                            op, 
                            recursiveTransform(leftExpressions, leftOperations, priorityLevel), 
                            recursiveTransform(rightExpressions, rightOperations, priorityLevel)
                            );
                    }
                    else if(op == ArithmeticOperation.SUBTRACTION) {
                        return new LLIRArithmetic(
                            ArithmeticOperation.SUM,
                            new LLIRArithmetic(ArithmeticOperation.SUBTRACTION, new LLIRInteger(0), recursiveTransform(rightExpressions, rightOperations, priorityLevel)), 
                            recursiveTransform(leftExpressions, leftOperations, priorityLevel)
                            );
                    }
                    else {
                        return new LLIRArithmetic(
                            op, 
                            recursiveTransform(rightExpressions, rightOperations, priorityLevel), 
                            recursiveTransform(leftExpressions, leftOperations, priorityLevel)
                            );
                    }
                }
                else if(priorityLevel == 3 && (operators.get(i) == ArithmeticOperation.MULTIPLICATION || operators.get(i) == ArithmeticOperation.DIVISION)) {
                    List<ArithmeticOperation> leftOperations = sliceOperators(operators, 0, i);
                    List<ArithmeticOperation> rightOperations = sliceOperators(operators, i+1, operators.size());

                    List<LLIRExpression> leftExpressions = sliceExpressions(expressions, 0, i+1);
                    List<LLIRExpression> rightExpressions = sliceExpressions(expressions, i+1, expressions.size());

                    ArithmeticOperation op = operators.get(i);

                    while(rightOperations.size() > 0 && (rightOperations.get(0) == ArithmeticOperation.MULTIPLICATION || rightOperations.get(0) == ArithmeticOperation.DIVISION)) {
                        leftOperations.add(op);
                        op = rightOperations.get(0);
                        rightOperations.remove(0);

                        leftExpressions.add(rightExpressions.get(0));
                        rightExpressions.remove(0);

                        i++;
                    }
                    return new LLIRArithmetic(
                        op, 
                        recursiveTransform(leftExpressions, leftOperations, priorityLevel), 
                        recursiveTransform(rightExpressions, rightOperations, priorityLevel)
                        );
                }
            }

            priorityLevel++;
        }

        return expressions.get(0);
    }

    private List<ArithmeticOperation> sliceOperators(List<ArithmeticOperation> ops, int from, int to) {
        List<ArithmeticOperation> sliced = new ArrayList<>();
        if(from < 0 || to > ops.size() || to - from <= 0) 
            return sliced;

        for(int i = from; i < to; i++) {
            sliced.add(ops.get(i));
        }
        return sliced;
    }

    private List<LLIRExpression> sliceExpressions(List<LLIRExpression> exps, int from, int to) {
        List<LLIRExpression> sliced = new ArrayList<>();
        if(from < 0 || to > exps.size() || to - from <= 0) 
            return sliced;

        for(int i = from; i < to; i++) {
            sliced.add(exps.get(i));
        }
        return sliced;
    }

    public void printArithmetic(LLIRArithmetic arithmetic) {
        printRecursive(arithmetic, "");
    }

    // ONLY FOR INTEGERS AND DEBUGGING PURPOSES
    private void printRecursive(LLIRArithmetic arithmetic, String space) {
        LLIRExpression left = arithmetic.getLeftExpression();
        LLIRExpression right = arithmetic.getRightExpression();
        
        if(left instanceof LLIRInteger) {
            LLIRInteger leftInt = (LLIRInteger) left;
            System.out.println(space + "LEFT: " + leftInt.getValue());
        }
        else {
            printRecursive((LLIRArithmetic) left, space + "\t");
        }

        System.out.println(space + "OPERATION: " + arithmetic.getOperation());

        if(right instanceof LLIRInteger) {
            LLIRInteger rightInt = (LLIRInteger) right;
            System.out.println(space + "RIGHT: " + rightInt.getValue());
        }
        else {
            printRecursive((LLIRArithmetic) right, space + "\t");
        }
    }
}