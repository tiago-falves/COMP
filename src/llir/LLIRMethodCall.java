package llir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import symbols.Descriptor;
import symbols.FunctionDescriptor;
import symbols.SymbolsTable;
import symbols.Type;
import symbols.VariableDescriptor;

public class LLIRMethodCall extends LLIRExpression {



    private  List<LLIRExpression> parametersExpressions;
    private  String methodName;
    private  SymbolsTable parametersTable;
    private  Type returnType;
    private  boolean isStatic;

    public LLIRMethodCall(List<LLIRExpression> parameters, String name, SymbolsTable parametersTable, Type returnType, boolean isStatic) {
        this.parametersExpressions = parameters;
        this.methodName = name;
        this.parametersTable = parametersTable;
        this.returnType = returnType;
        this.isStatic = isStatic;
    }
    public LLIRMethodCall(){
        parametersExpressions = new ArrayList<>();

    }

    /**
     * @return the method's name
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * @return the method's return type
     */
    public Type getReturnType() {
        return this.returnType;
    }

    /**
     * 
     * @return the parameters' types
     */
    public List<Type> getParameterTypes() {
        List<Type> parameterTypes = new ArrayList<>();

        if(this.parametersTable != null) {
            for (HashMap.Entry<String, List<Descriptor>> tableEntry : this.parametersTable.getTable().entrySet()) {
                if(tableEntry.getValue().size() > 0) {
                    VariableDescriptor parameter = (VariableDescriptor) tableEntry.getValue().get(0);
                    parameterTypes.add(parameter.getType());
                }
            }
        }

        return parameterTypes;

    }

    /**
     * 
     * @return the parameters' expressions
     */
    public List<LLIRExpression> getParametersExpressions() {
        return this.parametersExpressions;
    }

    /**
     * @return the parameters' symbols table
     */
    public SymbolsTable getParametersTable() {
        return parametersTable;
    }

    /**
     *
     * @return if it's static
     */

    public boolean isStatic() {
        return this.isStatic;
    }

    public void setParametersExpressions(List<LLIRExpression> parametersExpressions) {
        this.parametersExpressions = parametersExpressions;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParametersTable(SymbolsTable parametersTable) {
        this.parametersTable = parametersTable;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }



}