package llir;

import java.util.ArrayList;
import java.util.List;
import symbols.Type;

public class LLIRClassVariableInstantiation extends LLIRExpression {
    private String name;
    private List<LLIRExpression> parameters;
    private List<Type> importParameters;

    public LLIRClassVariableInstantiation(String variable) {
        this.name = variable;
        this.parameters = new ArrayList<>();
        this.importParameters = new ArrayList<>();
    }

    public List<LLIRExpression> getParameters(){
        return this.parameters;
    }

    public void addParameter(LLIRExpression expression){
        this.parameters.add(0, expression);
    } 

    public void addParameterTypes(List<Type> importParameters){
        this.importParameters = importParameters;
    }

    public List<Type> getParametersTypes(){
        return this.importParameters;
    }

    public String getName(){
        return name;
    }
}