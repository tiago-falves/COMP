package llir;

import symbols.ImportDescriptor;
import symbols.NamedTypeDescriptor;

import java.util.ArrayList;
import java.util.List;

public class LLIRImport extends LLIRExpression {
    public ImportDescriptor importDescriptor;


    private List<LLIRExpression> parametersExpressions;


    public LLIRImport(ImportDescriptor importDescriptor) {
        this.importDescriptor = importDescriptor;
        this.parametersExpressions = new ArrayList<>();
    }
    public LLIRImport() {
        this.parametersExpressions = new ArrayList<>();
    }

    /**
     * @param importDescriptor the importDescriptor to set
     */
    public void setImportDescriptor(ImportDescriptor importDescriptor) {
        this.importDescriptor = importDescriptor;
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



}