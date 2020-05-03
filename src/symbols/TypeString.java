package symbols;

public class TypeString {
    String type;

    public TypeString(String type) {
        this.type = type;
    }

    public Type parseType() {
        switch (this.type) {
            case "int":
                return Type.INT;        
            case "int[]":
                return Type.INT_ARRAY;
            case "void":
                return Type.VOID;
            case "boolean":
                return Type.BOOLEAN;
            case "String[]":
                return Type.STRING_ARRAY;
            case "class":
            default:
                return Type.CLASS;
        }
    }
}

