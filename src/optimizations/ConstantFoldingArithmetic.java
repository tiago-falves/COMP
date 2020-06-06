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
            }else if(arithmetic.getRightExpression() instanceof LLIRArithmetic){
                LLIRArithmetic rightArithmetic = (LLIRArithmetic)arithmetic.getRightExpression();
                
                if(rightArithmetic.getOperation() == ArithmeticOperation.SUBTRACTION 
                && rightArithmetic.getLeftExpression() instanceof LLIRInteger
                && rightArithmetic.getRightExpression() instanceof LLIRInteger){

                 // If the number on the left side isn't 0 this can't be a negative number 
                 if(((LLIRInteger)rightArithmetic.getLeftExpression()).getValue() == 0){
                     
                    int n1 = ((LLIRInteger)arithmetic.getLeftExpression()).getValue(), n2 = ((LLIRInteger)rightArithmetic.getRightExpression()).getValue()*-1;
                    
                    return calculateArithmetic(n1, n2, arithmetic.getOperation());                         
                 }
             }
            }
        }else if(arithmetic.getLeftExpression() instanceof LLIRArithmetic){
            LLIRArithmetic arith = (LLIRArithmetic)arithmetic.getLeftExpression();
            
            if(arith.getOperation() == ArithmeticOperation.SUBTRACTION 
               && arith.getLeftExpression() instanceof LLIRInteger
               && arith.getRightExpression() instanceof LLIRInteger){
                
                LLIRInteger rightInteger = (LLIRInteger)arith.getRightExpression();
                
                if(((LLIRInteger)arith.getLeftExpression()).getValue() == 0){
                    
                    if(arithmetic.getRightExpression() instanceof LLIRInteger){

                        int n1 = rightInteger.getValue()*-1, n2 = ((LLIRInteger)arithmetic.getRightExpression()).getValue();
                        
                        return calculateArithmetic(n1, n2, arithmetic.getOperation());      
                    }else if(arithmetic.getRightExpression() instanceof LLIRArithmetic){
                        LLIRArithmetic rightArithmetic = (LLIRArithmetic)arithmetic.getRightExpression();
                        
                        if(rightArithmetic.getOperation() == ArithmeticOperation.SUBTRACTION 
                           && rightArithmetic.getLeftExpression() instanceof LLIRInteger
                           && rightArithmetic.getRightExpression() instanceof LLIRInteger){
                               
                            if(((LLIRInteger)rightArithmetic.getLeftExpression()).getValue() == 0){
                                int n1 = rightInteger.getValue()*-1, n2 = ((LLIRInteger)rightArithmetic.getRightExpression()).getValue()*-1;
                                
                                return calculateArithmetic(n1, n2, arithmetic.getOperation());                                
                            }
                        }
            
                    }
                }
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

        return calculateArithmetic(n1, n2, arithmetic.getOperation());
    }

    private LLIRExpression calculateArithmetic(int n1, int n2, ArithmeticOperation operation){
        int result = 0;

        switch(operation){
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