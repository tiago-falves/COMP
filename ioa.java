public class ioa {
    public static void println(int a){System.out.println(a);};
    public static void println(boolean a){System.out.println(a);};
    public static void println(int[] a){
        System.out.print("Array: ");
        for(int i = 0; i < a.length; i++){
            System.out.print(a[i] + " ");
        }
        System.out.print("\n");
    }
}