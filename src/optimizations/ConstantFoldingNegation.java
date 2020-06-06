package optimizations;

import llir.LLIRBoolean;
import llir.LLIRConditional;
import llir.LLIRExpression;
import llir.LLIRNegation;
import llir.LLIRParenthesis;


public class ConstantFoldingNegation {
    private LLIRExpression negationExpression;

    public ConstantFoldingNegation(LLIRNegation negationExpression){
        this.negationExpression = this.transformNegation(negationExpression);
    }

    public LLIRExpression transformNegation(LLIRNegation negation){
        
        if(negation.getExpression() instanceof LLIRParenthesis){
            LLIRParenthesis parenthesis = (LLIRParenthesis)negation.getExpression();
            
            if(parenthesis.getExpression() instanceof LLIRNegation){
                ConstantFoldingNegation constantFoldingNegation = new ConstantFoldingNegation((LLIRNegation)parenthesis.getExpression());
                parenthesis.setExpression(constantFoldingNegation.getNegation());
            }else if(parenthesis.getExpression() instanceof LLIRConditional){
                ConstantFoldingConditional constantFoldingConditional = new ConstantFoldingConditional((LLIRConditional)parenthesis.getExpression());
                parenthesis.setExpression(constantFoldingConditional.getConditional());
            }
            
            if(parenthesis.getExpression() instanceof LLIRBoolean){
                negation.setExpression(parenthesis.getExpression());
            } 
        }


        if(negation.getExpression() instanceof LLIRBoolean){
            return calculateNegation((LLIRBoolean)negation.getExpression());
        }

        return negation;
    }

    public LLIRExpression getNegation(){
        return this.negationExpression;
    }

    private LLIRBoolean calculateNegation(LLIRBoolean booleanExpression){
        return new LLIRBoolean(!booleanExpression.getValue());
    }
}