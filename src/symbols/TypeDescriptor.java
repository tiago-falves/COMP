package symbols;

public abstract class TypeDescriptor extends Descriptor {
    protected Type type;
    protected String className;

    public TypeDescriptor(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setClassName(String className){
        if(this.type != Type.CLASS)
            return;
        
        this.className = className; 
    }

    public String getClassName(){
        if(this.type != Type.CLASS){
            return null;
        }
        
        return this.className;
    }
}
