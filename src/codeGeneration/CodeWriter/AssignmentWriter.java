package codeGeneration.CodeWriter;

import llir.LLIRAssignment;
import llir.LLIRBoolean;
import llir.LLIRExpression;
import llir.LLIRInteger;

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

    }
    public String getCode(){
        return this.code;
    }
}
