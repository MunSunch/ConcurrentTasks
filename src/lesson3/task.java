package lesson3;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class task {
    public static AtomicLong counter3 = new AtomicLong(0);
    public static AtomicLong counter4 = new AtomicLong(0);
    public static AtomicLong counter5 = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        var thread1 = new Thread(()->{
            for (var word: texts) {
                if(isConsistsOneLetter(word))
                    incrementCounter(word);
            }
        });

        var thread2 = new Thread(()->{
            for (var word: texts) {
                if(isAscendindSequence(word))
                    incrementCounter(word);
            }
        });

        var thread3 = new Thread(()->{
            for (var word: texts) {
                if(isPalindrome(word))
                    incrementCounter(word);
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("Красивых слов с длиной 3: " + counter3 + " шт");
        System.out.println("Красивых слов с длиной 4: " + counter4 + " шт");
        System.out.println("Красивых слов с длиной 5: " + counter5 + " шт");
    }

    private static void incrementCounter(String word) {
        if(word.length() == 3) {
            counter3.incrementAndGet();
        } else if(word.length() == 4) {
            counter4.incrementAndGet();
        } else if(word.length() == 5) {
            counter5.incrementAndGet();
        }
    }

    private static boolean isPalindrome(String word) {
        for (int i = 0; i < word.length()/2; i++) {
            if(word.charAt(i) != word.charAt(word.length()-i-1))
                return false;
        }
        return true;
    }

    private static boolean isAscendindSequence(String word) {
        for (int i = 1; i < word.length(); i++) {
            if(word.charAt(i-1) > word.charAt(i))
                return false;
        }
        return true;
    }

    private static boolean isConsistsOneLetter(String word) {
        for(var symbol: word.toCharArray()) {
            char firstSymbol = word.charAt(0);
            if(symbol != firstSymbol)
                return false;
        }
        return true;
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}

