//import io.println(int);
class SimpleTrue {
    public static void main(String[] args) {
        int a;
        int b;
        SimpleTrue simple;
        int c;
        a = 30; //Esta nao devia ser a segunda variavel por causa dos arguments? Deve fazer s ecaso especial para o main?
        b = 0 - 20;
        a = a + b;

        simple = new SimpleTrue();
        c = simple.add(a,b); //Aqui que aload e que se deve fazer?
        //io.println(c);
    }
    public int add(int a, int b){
        return a+b;
    }
}