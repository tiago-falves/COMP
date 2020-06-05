package optimizations;

import llir.ArithmeticOperation;
import llir.LLIRArithmetic;
import llir.LLIRExpression;
import llir.LLIRInteger;


public class ConstantFolding {
    private LLIRExpression arithmeticExpression;

    public ConstantFolding(LLIRArithmetic arithmeticExpression){
        this.arithmeticExpression = this.transformArithmetic(arithmeticExpression);
        System.out.println();
    }

    public LLIRExpression transformArithmetic(LLIRArithmetic arithmetic){
        System.out.println(arithmetic.getOperation());
        if(arithmetic.getLeftExpression() instanceof LLIRArithmetic){
            System.out.println("asdgfasdf");
            LLIRArithmetic leftExpression = (LLIRArithmetic)arithmetic.getLeftExpression();
            arithmetic.setLeftExpression(transformArithmetic(leftExpression));
            System.out.println("asssa");
        }
        if(arithmetic.getRightExpression() instanceof LLIRArithmetic){
            System.out.println("dsgjhvcbdzkjqgfdklrjlkd");
            LLIRArithmetic rightExpression = (LLIRArithmetic)arithmetic.getRightExpression();
            arithmetic.setRightExpression(transformArithmetic(rightExpression));
            System.out.println("hgfds");
        }
        
        if(arithmetic.getLeftExpression() instanceof LLIRInteger){
            System.out.print(((LLIRInteger)arithmetic.getLeftExpression()).getValue() +" :");
            if(arithmetic.getRightExpression() instanceof LLIRInteger){
                System.out.println(((LLIRInteger)arithmetic.getRightExpression()).getValue());
                LLIRExpression expression = calculateArithmetic(arithmetic);
                return expression;
            }
            System.out.println();
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