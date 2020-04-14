
public class Descriptor {
    private String name;
    private String type;
    private String offset;
    private boolean initialized;

    public Descriptor(String name, String type, String offset) {
        this.name = name;
        this.type = type;
        this.offset = offset;
        this.initialized = false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }    
    
    public String getOffset() {
        return this.offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }  
    
    public boolean isInitialized() {
        return this.initialized;
    }

    public void makeInitialized() {
        this.initialized = true;
    }
}