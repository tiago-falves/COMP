package codeGeneration;
import symbols.StringType;
import symbols.Type;
import symbols.TypeDescriptor;

import java.util.HashMap;

public class CodeGeneratorConstants {
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
        store.put(Type.BOOLEAN, "\tistore_");

        load.put(Type.INT, "\tiload_");
        load.put(Type.BOOLEAN, "\tiload_");


    }

    public static String getJvmType(TypeDescriptor typeDescriptor){
        Type type = typeDescriptor.getType();
        String className = typeDescriptor.getClassName();

        String jvmType = CodeGeneratorConstants.types.get(type);
        if(jvmType != null){
            return jvmType;
        }else{
            return className;
        }

    }
}
