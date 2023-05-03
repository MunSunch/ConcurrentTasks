package lesson1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Test {
    public static void main(String[] args) throws Exception {
        int countWords = 25;
        String[] texts = new String[countWords];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        ExecutorService service = Executors.newFixedThreadPool(countWords);
        List<Future<Integer>> futures = new ArrayList<>(countWords);
        long startTs = System.currentTimeMillis(); // start time
        for (String text : texts) {
            Callable<Integer> callable = () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                return maxSize;
            };
            var future = service.submit(callable);
            futures.add(future);
        }
        long endTs = System.currentTimeMillis(); // end time

        int maxCount = 0;
        for(var future: futures) {
            int currentCount = future.get();
            maxCount = Math.max(maxCount, currentCount);
        }
        service.shutdownNow();

        System.out.println("Time: " + (endTs - startTs) + "ms");
        System.out.println("Max: " + maxCount);
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
