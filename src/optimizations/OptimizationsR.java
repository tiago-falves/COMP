package optimizations;

import llir.LLIRNode;
import symbols.Descriptor;

import java.util.*;

public class OptimizationsR {

    //O variable Index remoceça em cada função? aqui ja nao e suposto right?
    //Mais vale usar Strings?
    public static LinkedHashMap<Integer, List<String>> def = new LinkedHashMap<>(); //Statement Number Variable Indexes
    public static LinkedHashMap<Integer, List<String>> use = new LinkedHashMap<>(); //Statement Number Variable Indexes
    public static LinkedHashMap<Integer, List<Integer>> succ = new LinkedHashMap<>(); //Statement Number Successors
    public static LinkedHashMap<Integer, List<Integer>> pred = new LinkedHashMap<>(); //Statement Number Predecessors
    public static int currentLine = 0;

    public static void incrementLine(){
        currentLine++;
        List<String> defs = new ArrayList<>();
        List<String> uses = new ArrayList<>();
        List<Integer> succs = new ArrayList<>();
        List<Integer> preds = new ArrayList<>();
        def.put(currentLine,defs);
        use.put(currentLine,uses);
        succ.put(currentLine,succs);
        pred.put(currentLine,preds);

    }

    public static void addDef(String variableName){
        List<String> defsList;
        if (def.containsKey(currentLine)){ //Nao e muito necessario
            defsList = def.get(currentLine);
            defsList.add(variableName);
        }else{
            defsList = new ArrayList<>();
            defsList.add(variableName);
            def.put(currentLine,defsList);
        }

    }

    public static void addUse(String variableName){
        List<String> usesList;
        if (use.containsKey(currentLine)){ //Nao e muito necessario
            usesList = use.get(currentLine);
            usesList.add(variableName);
        }else{
            usesList = new ArrayList<>();
            usesList.add(variableName);
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

    public static void addPredSucc(){
        if(currentLine > 1){
            addPred(currentLine-1);
        }
        addSucc(currentLine+1);
    }

    public static void print() {
        String s = "";
        System.out.println(currentLine);
        for (int i = 1; i <= currentLine; i++) {
            System.out.println("Statement " + i);
            List<String> defs = def.get(i);
            List<String> uses = use.get(i);
            for(String defName : defs) System.out.println("\tDef: " + defName);
            for(String useName : uses) System.out.println("\tUse: " + useName);

        }

        System.out.println(s);
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