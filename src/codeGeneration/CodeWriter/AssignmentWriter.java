package codeGeneration.CodeWriter;

import llir.*;

public class AssignmentWriter {

    private String code;

    public AssignmentWriter(LLIRAssignment assignment) {

        this.code  = "";

        String name = assignment.getVariable().getVariable().getName();
        LLIRExpression expression = assignment.getExpression();

        if(expression instanceof LLIRInteger) {
            IntegerWriter integerWriter = new IntegerWriter((LLIRInteger) expression,name);
            this.code += integerWriter.getCode();
        }
        else if (expression instanceof LLIRBoolean) {

            BooleanWriter booleanWriter = new BooleanWriter((LLIRBoolean) expression,name);
            this.code += booleanWriter.getCode();
        }
        else if (expression instanceof LLIRArithmetic) {

        }

    }
    public String getCode(){
        return this.code;
    }
}
