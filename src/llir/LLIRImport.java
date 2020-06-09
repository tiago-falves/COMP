package llir;

import symbols.ImportDescriptor;
import symbols.NamedTypeDescriptor;

import java.util.ArrayList;
import java.util.List;

public class LLIRImport extends LLIRExpression {
    public ImportDescriptor importDescriptor;

    private String variableName;

    private boolean isIsolated;

    private List<LLIRExpression> parametersExpressions;


    public LLIRImport(ImportDescriptor importDescriptor) {
        this.importDescriptor = importDescriptor;
        this.parametersExpressions = new ArrayList<>();
        this.isIsolated = false;
        this.variableName = null;
    }

    /**
     * @return the importDescriptor
     */
    public ImportDescriptor getImportDescriptor() {
        return importDescriptor;
    }

    public List<LLIRExpression> getParametersExpressions() {
        return parametersExpressions;
    }

    public void setParametersExpressions(List<LLIRExpression> parametersExpressions) {
        this.parametersExpressions = parametersExpressions;
    }


    /**
     * @return if it's an isolated call
     */
    public boolean isIsolated() {
        return isIsolated;
    }

    public void setIsolated(boolean isolated) {
        this.isIsolated = isolated;
    }

    public void setVariableName(String name){
        this.variableName = name;
    }

    public String getVariableName(){
        return this.variableName;
    }
}