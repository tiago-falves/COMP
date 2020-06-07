public class Pair {
    private int x;
    private int y;

    public Pair(){
        this.x = 0;
        this.y = 0;
    }

    public Pair(int n){
        this.x = n;
        this.y = n;
    }

    public Pair(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void printPair(){
        System.out.println("("+this.x+","+this.y+")");
    }
}