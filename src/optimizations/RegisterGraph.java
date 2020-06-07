package optimizations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RegisterGraph {
    public LinkedHashMap<String, VariableNode> variables;
    public LinkedHashMap<Integer, List<String>> in;
    public List<VariableNode> stack;
    public LinkedHashMap<String, Integer> allocation;
    public int currentRegister;
    
    public RegisterGraph(LinkedHashMap<Integer, List<String>> in) {
        this.variables = new LinkedHashMap<>();
        this.in = in;
        this.stack = new ArrayList<>();
        this.allocation = new LinkedHashMap<>();
        this.currentRegister = 1;
    }

    public LinkedHashMap<String, Integer> getAllocation() {
        return allocation;
    }

    public boolean colorGraph() { 
        int previousStackSize = 0;
        do {
            previousStackSize = stack.size();

            for(String key : variables.keySet()) {
                VariableNode currentVariable = variables.get(key);
                if(!currentVariable.isRemoved() && currentVariable.getNumEdges() < OptimizationManager.maximumLocalVariables) {
                    stack.add(currentVariable);
                    currentVariable.setRemoved(true);
                }
            }

        } while(previousStackSize != stack.size());

        if(checkIfEmpty())
            return false;

        for(int i = stack.size()-1; i >= 0; i--) {
            VariableNode variable = stack.get(i);

            while(variable.edgesHaveRegister(currentRegister)) {
                currentRegister++;
                if(currentRegister > OptimizationManager.maximumLocalVariables)
                    return false;
            }

            int assignedRegister = currentRegister;
            variable.setRegister(assignedRegister);
            allocation.put(variable.getName(), assignedRegister);
        }

        return true;
    }

    public void populateGraph() {
        for(int currentStatement = 0; currentStatement < OptimizationsR.currentLine; currentStatement++) {
            List<String> variableNames = in.get(currentStatement);

            if(variableNames.size() > 0)
                addNode(variableNames.get(0), currentStatement);
        }

    }

    private void addNode(String variableName, int currentStatement) {
        if(variables.containsKey(variableName)) return;

        VariableNode node = new VariableNode(variableName);
        variables.put(variableName, node);
        
        List<String> activeVariables = in.get(currentStatement);
        for(String activeVariableName : activeVariables) {
            if(activeVariableName == variableName) continue;

            VariableNode activeVariable;
            if(variables.containsKey(activeVariableName))
                activeVariable = variables.get(activeVariableName);
            else
                activeVariable = new VariableNode(activeVariableName);

            activeVariable.addEdge(node);
            node.addEdge(activeVariable);
        }
    }

    private boolean checkIfEmpty() {
        for(String key : variables.keySet()) {
            if(!variables.get(key).isRemoved()) return false;
        }

        return true;
    }
}