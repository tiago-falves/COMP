package optimizations;

import java.util.LinkedHashMap;

public class VariableNode {
    private String name;
    private boolean removed;
    private LinkedHashMap<String, VariableNode> edges;
    private int register;

    public VariableNode(String name) {
        this.name = name;
        this.removed = false;
        this.edges = new LinkedHashMap<>();
        this.register = -1;
    }

    public String getName() {
        return name;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public void addEdge(VariableNode variableNode) {
        if(variableNode.getName() == name) return;
        
        if(!edges.containsKey(variableNode.getName()))
            edges.put(variableNode.getName(), variableNode);
    }

    public boolean hasEdge(VariableNode variableNode) {
        String key = variableNode.getName();
        if(edges.containsKey(key) && !edges.get(key).isRemoved())
            return true;

        return false;
    }

    public int getNumEdges() {
        int num = 0;

        for(String key : edges.keySet()) {
            if(!edges.get(key).isRemoved()) num++;
        }

        return num;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int registerNum) {
        this.register = registerNum;
    }

    public boolean edgesHaveRegister(int registerNum) {
        for(String key : edges.keySet()) {
            if(edges.get(key).getRegister() == registerNum) return true;
        }

        return false;
    }
}