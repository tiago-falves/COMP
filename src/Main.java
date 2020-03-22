public class Main {
	
	public static void main(String[] args) {
		System.out.println("Welcome to Javamm Compiler");
        Javamm myJavamm = new Javamm(System.in);
		
		try {
            SimpleNode root = myJavamm.JavaMM(); // returns reference to root node
        	
		    root.dump(""); // prints the tree on the screen

        } catch(Exception e) {

		}
	}	
}