package optimizations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RegisterGraph {
    public LinkedHashMap<String, VariableNode> variables;
    public LinkedHashMap<Integer, List<String>> in;
    public LinkedHashMap<Integer, List<String>> out;
    public List<VariableNode> stack;
    public LinkedHashMap<String, Integer> colors;
    public int currentColor;
    
    public RegisterGraph(LinkedHashMap<Integer, List<String>> in, LinkedHashMap<Integer, List<String>> out) {
        this.variables = new LinkedHashMap<>();
        this.in = in;
        this.out = out;
        this.stack = new ArrayList<>();
        this.colors = new LinkedHashMap<>();
        this.currentColor = 1;
    }

    public LinkedHashMap<String, Integer> getColors() {
        return colors;
    }

    public int getCurrentColor() {
        return currentColor;
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

        if(!checkIfEmpty()) {
            int offset = 0;
            do {
                offset++;
                do {
                    previousStackSize = stack.size();
        
                    for(String key : variables.keySet()) {
                        VariableNode currentVariable = variables.get(key);
                        if(!currentVariable.isRemoved() && currentVariable.getNumEdges() < (OptimizationManager.maximumLocalVariables + offset)) {
                            stack.add(currentVariable);
                            currentVariable.setRemoved(true);
                        }
                    }
        
                } while(previousStackSize != stack.size());
            } while(!checkIfEmpty());

            for(int i = stack.size()-1; i >= 0; i--) {
                VariableNode variable = stack.get(i);
    
                while(variable.edgesHaveColor(currentColor)) {
                    currentColor++;
                }
                int assignedColor = currentColor;
                variable.setColor(assignedColor);
            }

            return false;
        }

        for(int i = stack.size()-1; i >= 0; i--) {
            VariableNode variable = stack.get(i);

            while(variable.edgesHaveColor(currentColor)) {
                currentColor++;
                if(currentColor > OptimizationManager.maximumLocalVariables)
                    return false;
            }

            int assignedColor = currentColor;
            variable.setColor(assignedColor);
            colors.put(variable.getName(), assignedColor);
        }

        return true;
    }

    public void populateGraph() {
        for(int currentStatement = 0; currentStatement < RegisterReducer.currentLine; currentStatement++) {
            List<String> liveInVariables = in.get(currentStatement);
            if(liveInVariables == null) liveInVariables = new ArrayList<>();

            List<String> liveOutVariables = out.get(currentStatement);
            if(liveOutVariables == null) liveOutVariables = new ArrayList<>();

            List<String> liveVariables = getLiveAtStatement(liveInVariables, liveOutVariables);

            if(liveVariables.size() > 0) addNodes(liveVariables);
        }

    }

    private void addNodes(List<String> variableNames) {
        for(String var1 : variableNames) {
            VariableNode varNode1;
            if(variables.containsKey(var1)) {
                varNode1 = variables.get(var1);
            } else {
                varNode1 = new VariableNode(var1);
                variables.put(var1, varNode1);
            }

            for(String var2 : variableNames) {
                if(var1 == var2) continue;

                VariableNode varNode2;
                if(variables.containsKey(var2)) {
                    varNode2 = variables.get(var2);
                } else {
                    varNode2 = new VariableNode(var2);
                    variables.put(var2, varNode2);
                }

                if(!varNode1.hasEdge(varNode2)) varNode1.addEdge(varNode2);
                if(!varNode2.hasEdge(varNode1)) varNode2.addEdge(varNode1);
            }
        }

    }

    private boolean checkIfEmpty() {
        for(String key : variables.keySet()) {
            if(!variables.get(key).isRemoved()) return false;
        }

        return true;
    }

    private List<String> getLiveAtStatement(List<String> inAtStatement, List<String> outAtStatement) {
        Set<String> liveSet = new LinkedHashSet<>(inAtStatement);
        liveSet.addAll(outAtStatement);
        return new ArrayList<>(liveSet);
    }

    public void printGraph() {
        String s = "PRINTING GRAPH\n";

        for(String key : variables.keySet()) {
            VariableNode var = variables.get(key);
            s += var.getName() + "\n";

            for(String edgeKey : var.getEdges().keySet()) {
                s += "\t" + var.getEdges().get(edgeKey).getName() + "\n";
            }
        }
        s += "\n";

        System.out.println(s);
    }

    public void printStack() {
        String s = "PRINTING STACK\n";

        for(int i = stack.size() - 1; i >= 0; i--) {
            s += stack.get(i).getName() + "\n";
        }
        s += "\n";

        System.out.println(s);
    }
    
}