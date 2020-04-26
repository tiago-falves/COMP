package codeGeneration;
import java.util.HashMap;

public class CodeGeneratorConstants {
    public static HashMap<String, String> types;
    public static HashMap<String, String> store;
    public static HashMap<String, String> load;
    public static HashMap<String, String> arithmeticOperators;

    static {
        types = new HashMap<>();
        store = new HashMap<>();
        load = new HashMap<>();
        arithmeticOperators = new HashMap<>();

        arithmeticOperators.put("+", "\tiadd");
        arithmeticOperators.put("-", "\tisub");
        arithmeticOperators.put("*", "\timul");
        arithmeticOperators.put("/", "\tidiv");

        types.put("int", "I");
        types.put("boolean", "Z");
        types.put("int[]", "[I");
        types.put("void", "V");
        types.put("String[]", "[Ljava/lang/String;"); //TODO Quase certo que isto nao esta bem

        store.put("int", "\tistore ?");
        store.put("boolean", "\tistore ?");

        load.put("int", "\tiload ?");
        load.put("boolean", "\tiload ?");


    }
}
