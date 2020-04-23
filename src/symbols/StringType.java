package symbols;

public class StringType {
    Type type;

    public StringType(Type type) {
        this.type = type;
    }

    public String getString() {
        switch (this.type) {
            case INT:
                return "int";        
            case INT_ARRAY: 
                return "int[]";
            case VOID:
                return "void";
            case BOOLEAN:
                return "boolean";
            case CLASS: 
                return "class";
            case STRING_ARRAY: 
                return "String[]";
            //TODO: VER ISTO
            default:
                return "void";
        }
    }
}

