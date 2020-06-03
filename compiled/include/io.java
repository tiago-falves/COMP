import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class io {
 public static void println(int x){
  System.out.println(x);
 }

 public static void print(int x){
  System.out.println(x);
 }

 public static int read(){
  try{
   BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
   String name = reader.readLine();
   return Integer.parseInt(name);
  }catch(IOException e){
   return -1;
  }
 }
}
