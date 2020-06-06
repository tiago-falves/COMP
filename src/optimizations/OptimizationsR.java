package optimizations;

import codeGeneration.CodeWriter.BlockStatementWriter;
import codeGeneration.CodeWriter.WhileWriter;
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
    public static LinkedHashMap<Integer, List<String>> in = new LinkedHashMap<>(); //Statement Number Predecessors
    public static LinkedHashMap<Integer, List<String>> out = new LinkedHashMap<>(); //Statement Number Predecessors
    public static boolean firstPass = true;
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

    public static void addSucc(int successor,int id){
        List<Integer> succsList;
        if (succ.containsKey(id)){ //Nao e muito necessario
            succsList = succ.get(id);
            succsList.add(successor);
        }else{
            succsList = new ArrayList<>();
            succsList.add(successor);
            succ.put(id,succsList);
        }
    }
    public static void addSuccIfExists(int successor,int id){
        List<Integer> succsList;
        if (succ.containsKey(id)){ //Nao e muito necessario
            succsList = succ.get(id);
            succsList.add(successor);
        }
    }

    public static void addPredIfExists(int predec,int id){
        List<Integer> predList;
        if (pred.containsKey(id)){ //Nao e muito necessario
            predList = pred.get(id);
            predList.add(predec);
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

    public static void addBlockPredSuccExpression(BlockStatementWriter blockStatementWriter,int expressionStatement){
        if(blockStatementWriter instanceof WhileWriter){
            addPredIfExists(currentLine,expressionStatement);
            addSuccIfExists(expressionStatement,currentLine);
        }
        addSucc(currentLine+1,expressionStatement);

    }

    public static void addPredSuccWhile(BlockStatementWriter blockStatementWriter,int i){
        addPredSucc();
        if(blockStatementWriter instanceof WhileWriter){
            if(i == 0){

            }
        }
        if(currentLine > 1){
            addPred(currentLine-1);
        }
        addSucc(currentLine+1);
    }

    public static void calculateInOut(){

        boolean condition = true;

        LinkedHashMap<Integer, List<String>> in_tmp = new LinkedHashMap<>();
        LinkedHashMap<Integer, List<String>> out_tmp = new LinkedHashMap<>();

        do {
            condition = true;
            for (int i = 1; i <= currentLine; i++) {
                in_tmp.put(i,in.get(i));
                out_tmp.put(i,out.get(i));

                for (Integer successor :succ.get(i)) {
                    out.put(i,addWithoutDuplicates(out.get(i),in.get(successor)));
                }

                List<String> removedDuplicates = removeSubSet(out.get(i),def.get(i));
                in.put(i,addWithoutDuplicates(use.get(i),removedDuplicates));
            }

            for (int i = 1; i <= currentLine ; i++) {
                if(!((in_tmp.get(i).equals(in.get(i))) && (out_tmp.get(i).equals(out.get(i))))){
                    condition = false;
                    break;
                }
            }

        } while (condition);
    }

    public static List<String> removeSubSet(List<String> list1, List<String> list2){
        // Prepare a union
        List<String> union = new ArrayList<String>(list1);
        // Prepare an intersection
        List<String> intersection = new ArrayList<String>(list2);
        // Subtract the intersection from the union
        union.removeAll(intersection);
        return union;
    }

    public static List<String> addWithoutDuplicates(List<String> one, List<String> two){
        Set<String> fooSet = new LinkedHashSet<>(one);
        fooSet.addAll(two);
        return new ArrayList<>(fooSet);
    }

    public static void reset() {
        def = new LinkedHashMap<>();
        use = new LinkedHashMap<>();
        succ = new LinkedHashMap<>();
        pred = new LinkedHashMap<>();
        in = new LinkedHashMap<>();
        out = new LinkedHashMap<>();
        firstPass = true;
        currentLine = 0;
    }

    public static void print() {
        String s = "";
        System.out.println(currentLine);
        for (int i = 1; i <= currentLine; i++) {
            System.out.println("Statement " + i);
            List<String> defs = def.get(i);
            List<String> uses = use.get(i);
            List<Integer> preds = pred.get(i);
            List<Integer> succs = succ.get(i);

            for(String defName : defs) System.out.println("\tDef: " + defName);
            for(String useName : uses) System.out.println("\tUse: " + useName);
            for(Integer predInt : preds) System.out.println("\tPred: " + predInt);
            for(Integer sucssInt : succs) System.out.println("\tSucc: " + sucssInt);
        }

        System.out.println(s);
    }

}




