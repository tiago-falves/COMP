package codeGeneration.CodeWriter;

import codeGeneration.CGConst;
import codeGeneration.FunctionBody;
import llir.*;
import symbols.Type;

public class ArithmeticWriter {
    private String code;
    private LLIRArithmetic arithmetic;

    public ArithmeticWriter(LLIRArithmetic arithmetic, String name){
        this.code  = "";
        this.arithmetic = arithmetic;
        this.code += generateLeftCode(arithmetic.getLeftExpression(),name);
        this.code += generateRightCode(arithmetic.getRightExpression(),name);
        this.code += CGConst.arithmeticOperators.get(arithmetic.getOperation()) + "\n";
    }

    private String generateLeftCode(LLIRExpression expression,String name){

        String result = new String();
        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression,name);
            result += integerWriter.getCode();
        }
        else if(expression instanceof LLIRVariable) {
            VariableWriter variableWriter = new VariableWriter((LLIRVariable) expression);
            result += variableWriter.getCode();
        }
        else if (expression instanceof LLIRArithmetic) {
            ArithmeticWriter arithmeticWriter = new ArithmeticWriter((LLIRArithmetic) expression,name);
            result += arithmeticWriter.getCode();
        }
        else if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            result += methodCallWriter.getCode();
        }

        /*
        System.out.println();
        System.out.println("LEFT CODE");
        System.out.println(result);
        System.out.println("\n");
        */

        return result;
    }

    private String generateRightCode(LLIRExpression expression, String name){

        String result = new String();
        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression,name);
            result += integerWriter.getCode();

        }
        else if(expression instanceof LLIRVariable) {
            VariableWriter variableWriter = new VariableWriter((LLIRVariable) expression);
            result += variableWriter.getCode();

        }
        else if (expression instanceof LLIRArithmetic) {
            ArithmeticWriter arithmeticWriter = new ArithmeticWriter((LLIRArithmetic) expression,name);
            result += arithmeticWriter.getCode();
        }
        else if (expression instanceof LLIRMethodCall) {
            MethodCallWriter methodCallWriter = new MethodCallWriter((LLIRMethodCall) expression);
            result += methodCallWriter.getCode();

        }

        /*
        System.out.println();
        System.out.println("RIGHT CODE");
        System.out.println(result);
        System.out.println("\n");
        */

        return result;
    }

    public String getCode(){
        return this.code;
    }

}
