package llir;

public enum ConditionalOperation {
    LESS_THAN("<"),
    AND("&&"),
    NEGATION("!");

    private final String operation;

    ConditionalOperation(String operation) {
        this.operation = operation;
    }



};
