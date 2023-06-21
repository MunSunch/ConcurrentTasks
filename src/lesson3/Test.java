package lesson3;

public class Test {
    public static volatile long a = 0;
    public static volatile long b = 100;

    public static void main(String[] args) {
        new Thread(()->{
            b = -100;
            a = 1;
        }).start();

        new Thread(()->{
            while(a!=1){}
            System.out.println(b);
        }).start();
    }
}
