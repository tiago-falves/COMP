import java.util.Random;

public class MathUtils {
    public static int random(int lowerLimit, int upperLimit){
        return (new Random().nextInt(upperLimit-lowerLimit))+lowerLimit;
    }
}