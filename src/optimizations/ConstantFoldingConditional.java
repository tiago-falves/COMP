package optimizations;

import llir.ArithmeticOperation;
import llir.LLIRArithmetic;
import llir.LLIRBoolean;
import llir.LLIRConditional;
import llir.LLIRExpression;
import llir.LLIRInteger;
import llir.LLIRNegation;
import llir.LLIRParenthesis;


public class ConstantFoldingConditional {
    private LLIRExpression conditionalExpression;

    public ConstantFoldingConditional(LLIRConditional conditionalExpression){
        this.conditionalExpression = this.transformConditional(conditionalExpression);
    }

    public LLIRExpression transformConditional(LLIRConditional conditional){
        switch(conditional.getOperation()){
            case LESS_THAN:
                return transformConditionalLessThan(conditional);
            case AND:
                return transformConditionalAnd(conditional);
        }

        return conditional;
    }

    public LLIRExpression getConditional(){
        return this.conditionalExpression;
    }

    private LLIRExpression transformConditionalAnd(LLIRConditional conditional){
        if(conditional.getLeftExpression() instanceof LLIRConditional){
            LLIRConditional leftExpression = (LLIRConditional)conditional.getLeftExpression();
            conditional.setLeftExpression(transformConditional(leftExpression));
        }else if(conditional.getLeftExpression() instanceof LLIRNegation){
            ConstantFoldingNegation constantFoldingNegation = new ConstantFoldingNegation((LLIRNegation)conditional.getLeftExpression());
            conditional.setLeftExpression(constantFoldingNegation.getNegation());
        }else if(conditional.getLeftExpression() instanceof LLIRParenthesis){
            LLIRParenthesis parenthesis = (LLIRParenthesis)conditional.getLeftExpression();
            
            if(parenthesis.getExpression() instanceof LLIRNegation){
                ConstantFoldingNegation constantFoldingNegation = new ConstantFoldingNegation((LLIRNegation)parenthesis.getExpression());
                parenthesis.setExpression(constantFoldingNegation.getNegation());
            }else if(parenthesis.getExpression() instanceof LLIRConditional){
                ConstantFoldingConditional constantFoldingConditional = new ConstantFoldingConditional((LLIRConditional)parenthesis.getExpression());
                parenthesis.setExpression(constantFoldingConditional.getConditional());
            }
            
            if(parenthesis.getExpression() instanceof LLIRBoolean){
                conditional.setLeftExpression(parenthesis.getExpression());
            }
        }

        if(conditional.getRightExpression() instanceof LLIRConditional){
            LLIRConditional rightExpression = (LLIRConditional)conditional.getRightExpression();
            conditional.setRightExpression(transformConditional(rightExpression));
        }else if(conditional.getRightExpression() instanceof LLIRNegation){
            ConstantFoldingNegation constantFoldingNegation = new ConstantFoldingNegation((LLIRNegation)conditional.getRightExpression());
            conditional.setRightExpression(constantFoldingNegation.getNegation());
        }else if(conditional.getRightExpression() instanceof LLIRParenthesis){
            LLIRParenthesis parenthesis = (LLIRParenthesis)conditional.getRightExpression();
            
            if(parenthesis.getExpression() instanceof LLIRNegation){
                ConstantFoldingNegation constantFoldingNegation = new ConstantFoldingNegation((LLIRNegation)parenthesis.getExpression());
                parenthesis.setExpression(constantFoldingNegation.getNegation());
            }else if(parenthesis.getExpression() instanceof LLIRConditional){
                ConstantFoldingConditional constantFoldingConditional = new ConstantFoldingConditional((LLIRConditional)parenthesis.getExpression());
                parenthesis.setExpression(constantFoldingConditional.getConditional());
            }
            
            if(parenthesis.getExpression() instanceof LLIRBoolean){
                conditional.setRightExpression(parenthesis.getExpression());
            }
        }

        if(conditional.getLeftExpression() instanceof LLIRBoolean){
            LLIRBoolean booleanExpr = (LLIRBoolean)conditional.getLeftExpression();
            // Operator && with false always returns false
            if(!booleanExpr.getValue())
                return new LLIRBoolean(false);

            if(conditional.getRightExpression() instanceof LLIRBoolean){
                return calculateAnd(conditional);
            }
        }
        if(conditional.getRightExpression() instanceof LLIRBoolean){
            LLIRBoolean booleanExpr = (LLIRBoolean)conditional.getRightExpression();
            
            // Operator && with false always returns false
            if(!booleanExpr.getValue())
                return new LLIRBoolean(false);
        }

        return conditional;
    }

    private LLIRExpression transformConditionalLessThan(LLIRConditional conditional){
        if(conditional.getLeftExpression() instanceof LLIRArithmetic){
            ConstantFoldingArithmetic constantFoldingArithmetic = new ConstantFoldingArithmetic((LLIRArithmetic)conditional.getLeftExpression());
            conditional.setLeftExpression(constantFoldingArithmetic.getArithmetic());
        }else if(conditional.getLeftExpression() instanceof LLIRParenthesis){
            LLIRParenthesis parenthesis = (LLIRParenthesis)conditional.getLeftExpression();
            
            if(parenthesis.getExpression() instanceof LLIRArithmetic){
                ConstantFoldingArithmetic constantFoldingArithmetic = new ConstantFoldingArithmetic((LLIRArithmetic)parenthesis.getExpression());
                parenthesis.setExpression(constantFoldingArithmetic.getArithmetic());
            }
            
            if(parenthesis.getExpression() instanceof LLIRInteger){
                conditional.setLeftExpression(parenthesis.getExpression());
            }
        }

        if(conditional.getRightExpression() instanceof LLIRArithmetic){
            ConstantFoldingArithmetic constantFoldingArithmetic = new ConstantFoldingArithmetic((LLIRArithmetic)conditional.getRightExpression());
            conditional.setRightExpression(constantFoldingArithmetic.getArithmetic());
        }else if(conditional.getRightExpression() instanceof LLIRParenthesis){
            LLIRParenthesis parenthesis = (LLIRParenthesis)conditional.getRightExpression();
            
            if(parenthesis.getExpression() instanceof LLIRArithmetic){
                ConstantFoldingArithmetic constantFoldingArithmetic = new ConstantFoldingArithmetic((LLIRArithmetic)parenthesis.getExpression());
                parenthesis.setExpression(constantFoldingArithmetic.getArithmetic());
            }
            
            if(parenthesis.getExpression() instanceof LLIRInteger){
                conditional.setRightExpression(parenthesis.getExpression());
            }
        }

        if(conditional.getLeftExpression() instanceof LLIRInteger){
            if(conditional.getRightExpression() instanceof LLIRInteger){
                return calculateLessThan(conditional);
            }else if(conditional.getRightExpression() instanceof LLIRArithmetic){
                LLIRArithmetic rightArithmetic = (LLIRArithmetic)conditional.getRightExpression();
                
                // A negative number is always an arithmetic expression with 0 - abs(number)
                if(rightArithmetic.getOperation() == ArithmeticOperation.SUBTRACTION 
                   && rightArithmetic.getLeftExpression() instanceof LLIRInteger
                   && rightArithmetic.getRightExpression() instanceof LLIRInteger){
                    
                    // If the number on the left side isn't 0 this can't be a negative number 
                    if(((LLIRInteger)rightArithmetic.getLeftExpression()).getValue() == 0){
                        // A positive number is always greater than a negative number
                        return new LLIRBoolean(false);                                
                    }
                }
            }
        }else if(conditional.getLeftExpression() instanceof LLIRArithmetic){
            LLIRArithmetic arithmetic = (LLIRArithmetic)conditional.getLeftExpression();
            
            // A negative number is always an arithmetic expression with 0 - abs(number)
            if(arithmetic.getOperation() == ArithmeticOperation.SUBTRACTION 
               && arithmetic.getLeftExpression() instanceof LLIRInteger
               && arithmetic.getRightExpression() instanceof LLIRInteger){
                
                LLIRInteger rightInteger = (LLIRInteger)arithmetic.getRightExpression();

                // If the number on the left side isn't 0 this can't be a negative number 
                if(((LLIRInteger)arithmetic.getLeftExpression()).getValue() == 0){
                    if(conditional.getRightExpression() instanceof LLIRInteger){
                        // A negative number is always lower than a positive number
                        return new LLIRBoolean(true);
                    }else if(conditional.getRightExpression() instanceof LLIRArithmetic){
                        LLIRArithmetic rightArithmetic = (LLIRArithmetic)conditional.getRightExpression();
                        
                        // A negative number is always an arithmetic expression with 0 - abs(number)
                        if(rightArithmetic.getOperation() == ArithmeticOperation.SUBTRACTION 
                           && rightArithmetic.getLeftExpression() instanceof LLIRInteger
                           && rightArithmetic.getRightExpression() instanceof LLIRInteger){
                            // If the number on the left side isn't 0 this can't be a negative number 
                            if(((LLIRInteger)rightArithmetic.getLeftExpression()).getValue() == 0){
                                LLIRInteger rightInteger2 = (LLIRInteger)rightArithmetic.getRightExpression();
                                return new LLIRBoolean(-1*rightInteger.getValue() < -1*rightInteger2.getValue());                                
                            }
                        }
            
                    }
                }
            }

        }

        return conditional;
    }

    private LLIRExpression calculateAnd(LLIRConditional conditional){
        boolean n1, n2;
        n1 = ((LLIRBoolean)conditional.getLeftExpression()).getValue();
        n2 = ((LLIRBoolean)conditional.getRightExpression()).getValue();

        return new LLIRBoolean(n1 && n2);
    }

    private LLIRExpression calculateLessThan(LLIRConditional conditional){
        int n1, n2;
        n1 = ((LLIRInteger)conditional.getLeftExpression()).getValue();
        n2 = ((LLIRInteger)conditional.getRightExpression()).getValue();

        return new LLIRBoolean(n1 < n2);
    }
}