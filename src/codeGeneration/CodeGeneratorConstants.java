package codeGeneration;
import java.util.HashMap;

public class CodeGeneratorConstants {
    public static HashMap<String, String> types;
    public static HashMap<String, String> store;
    public static HashMap<String, String> load;
    public static HashMap<String, String> arithmeticOperators;

    public CodeGeneratorConstants(){
        types = new HashMap<>();
        store = new HashMap<>();
        load = new HashMap<>();
        arithmeticOperators = new HashMap<>();

        arithmeticOperators.put("+", "\tiadd");
        arithmeticOperators.put("-", "\tisub");
        arithmeticOperators.put("*", "\timul");
        arithmeticOperators.put("/", "\tidiv");

        store.put("int", "\tistore ?");
        store.put("boolean", "\tistore ?");

        load.put("int", "\tiload ?");
        load.put("boolean", "\tiload ?");


    }
}
