package optimizations;

import llir.LLIRNode;
import symbols.Descriptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class OptimizationsR {

    public static LinkedHashMap<Integer, List<Integer>> def; //Statement Number Variable Indexes
    public static LinkedHashMap<Integer, List<Integer>> use; //Statement Number Variable Indexes
    public static LinkedHashMap<Integer, List<Integer>> succ; //Statement Number Successors
    public static LinkedHashMap<Integer, List<Integer>> pred; //Statement Number Predecessors
    public static int currentLine = 0;

    public OptimizationsR() {
        def = new LinkedHashMap<>();
        use = new LinkedHashMap<>();
        succ = new LinkedHashMap<>();
        pred = new LinkedHashMap<>();
    }

    public static void incrementLine(){
        currentLine++;
        def.put(currentLine,new ArrayList<>());
        use.put(currentLine,new ArrayList<>());
        succ.put(currentLine,new ArrayList<>());
        pred.put(currentLine,new ArrayList<>());

    }

    public static void addDef(int variableIndex){
        List<Integer> defsList;
        if (def.containsKey(currentLine)){ //Nao e muito necessario
            defsList = def.get(currentLine);
            defsList.add(variableIndex);
        }else{
            defsList = new ArrayList<>();
            defsList.add(variableIndex);
            def.put(currentLine,defsList);
        }

    }

    public static void addUse(int variableIndex){
        List<Integer> usesList;
        if (use.containsKey(currentLine)){ //Nao e muito necessario
            usesList = use.get(currentLine);
            usesList.add(variableIndex);
        }else{
            usesList = new ArrayList<>();
            usesList.add(variableIndex);
            use.put(currentLine,usesList);
        }
    }

    public static void addSucc(int successor){
        List<Integer> succsList;
        if (succ.containsKey(currentLine)){ //Nao e muito necessario
            succsList = succ.get(currentLine);
            succsList.add(successor);
        }else{
            succsList = new ArrayList<>();
            succsList.add(successor);
            succ.put(currentLine,succsList);
        }
    }

    public static void addPred(int predessor){
        List<Integer> predsList;
        if (pred.containsKey(currentLine)){ //Nao e muito necessario
            predsList = pred.get(currentLine);
            predsList.add(predessor);
        }else{
            predsList = new ArrayList<>();
            predsList.add(predessor);
            pred.put(currentLine,predsList);
        }
    }


}




/*for each n
        in[n]  {}; out[n]  {}
        repeat
        for each n
        in’[n]  in[n]; out’[n]  out[n]
        in[n]  use[n]  (out[n] – def [n])
        out[n]  in[s]
        until in’[n] = in[n] and out’[n] = out[n] for all n*/