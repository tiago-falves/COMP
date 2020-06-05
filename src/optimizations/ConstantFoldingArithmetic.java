package optimizations;

import llir.ArithmeticOperation;
import llir.LLIRArithmetic;
import llir.LLIRExpression;
import llir.LLIRInteger;


public class ConstantFoldingArithmetic {
    private LLIRExpression arithmeticExpression;

    public ConstantFoldingArithmetic(LLIRArithmetic arithmeticExpression){
        this.arithmeticExpression = this.transformArithmetic(arithmeticExpression);
    }

    public LLIRExpression transformArithmetic(LLIRArithmetic arithmetic){
        if(arithmetic.getLeftExpression() instanceof LLIRArithmetic){
            LLIRArithmetic leftExpression = (LLIRArithmetic)arithmetic.getLeftExpression();
            arithmetic.setLeftExpression(transformArithmetic(leftExpression));
        }
        if(arithmetic.getRightExpression() instanceof LLIRArithmetic){
            LLIRArithmetic rightExpression = (LLIRArithmetic)arithmetic.getRightExpression();
            arithmetic.setRightExpression(transformArithmetic(rightExpression));
        }
        
        if(arithmetic.getLeftExpression() instanceof LLIRInteger){
            if(arithmetic.getRightExpression() instanceof LLIRInteger){
                LLIRExpression expression = calculateArithmetic(arithmetic);
                return expression;
            }
        }

        return arithmetic;
    }

    public LLIRExpression getArithmetic(){
        return this.arithmeticExpression;
    }

    private LLIRExpression calculateArithmetic(LLIRArithmetic arithmetic){
        int n1, n2;
        n1 = ((LLIRInteger)arithmetic.getLeftExpression()).getValue();
        n2 = ((LLIRInteger)arithmetic.getRightExpression()).getValue();

        int result = 0;

        switch(arithmetic.getOperation()){
            case SUM:
                result = n1 + n2;
                break;
            case SUBTRACTION:
                result = n1 - n2;
                if(result < 0)
                    return new LLIRArithmetic(
                        ArithmeticOperation.SUBTRACTION, 
                        new LLIRInteger(0),
                        new LLIRInteger(Math.abs(result))
                    );
                break;
            case MULTIPLICATION:
                result = n1 * n2;
                break;
            case DIVISION:
                result = n1 / n2;
                break;
        }

        return new LLIRInteger(result);
    }
}