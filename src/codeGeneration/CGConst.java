package codeGeneration;
import symbols.Type;
import symbols.TypeDescriptor;

import java.util.HashMap;

public class CGConst {
    public static HashMap<Type, String> types;
    public static HashMap<Type, String> store;
    public static HashMap<Type, String> load;
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

        types.put(Type.INT, "I");
        types.put(Type.BOOLEAN, "Z");
        types.put(Type.INT, "[I");
        types.put(Type.VOID, "V");
        types.put(Type.STRING_ARRAY, "[Ljava/lang/String;"); //TODO Quase certo que isto nao esta bem

        store.put(Type.INT, "\tistore_");
        store.put(Type.BOOLEAN, "\tistore\t");

        load.put(Type.INT, "\tiload_");
        load.put(Type.BOOLEAN, "\tiload_");
        load.put(Type.STRING_ARRAY, "\taload_");
        load.put(Type.STRING, "\taload_");
        load.put(Type.CLASS, "\taload_");

    }

    public static String TRUE_VALUE = "\ticonst_1";
    public static String FALSE_VALUE = "\ticonst_0";


    public static String getJvmType(TypeDescriptor typeDescriptor){
        Type type = typeDescriptor.getType();
        String className = typeDescriptor.getClassName();

        String jvmType = CGConst.types.get(type);
        if(jvmType != null){
            return jvmType;
        }else{
            return className;
        }

    }
}