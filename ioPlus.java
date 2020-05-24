import java.util.Random;

class ioPlus {
  public static void printHelloWorld(){
    System.out.println("Hello World");
  }
  
  public static int requestNumber(){
    return new Random().nextInt();
  }

  public static void printResult(int result){
    System.out.println(result);
  }
}
