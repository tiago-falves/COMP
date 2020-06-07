package optimizations;

import java.util.LinkedHashMap;

public class VariableNode {
    private String name;
    private boolean removed;
    private LinkedHashMap<String, VariableNode> edges;
    private int color;

    public VariableNode(String name) {
        this.name = name;
        this.removed = false;
        this.edges = new LinkedHashMap<>();
        this.color = -1;
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

    public LinkedHashMap<String, VariableNode> getEdges() {
        return edges;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int colorNum) {
        this.color = colorNum;
    }

    public boolean edgesHaveColor(int colorNum) {
        for(String key : edges.keySet()) {
            if(edges.get(key).getColor() == colorNum) return true;
        }

        return false;
    }
}