package lesson2;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class task1 {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int countThread = 1000;

        Runnable countingMaxThread = () -> {
            while(!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Count max thread close...");
                        return;
                    }

                    Map.Entry<Integer, Integer> maxEntry = new AbstractMap.SimpleEntry<>(0,0);
                    for(var entry: sizeToFreq.entrySet()) {
                        if(entry.getValue() > maxEntry.getValue()) {
                            maxEntry = entry;
                        }
                    }
                    System.out.println("Count max thread: "+maxEntry.getKey()+" ("+maxEntry.getValue()+" раз)");

                    sizeToFreq.notify();
                }
            }
        };
        Thread countingThread = new Thread(countingMaxThread);
        countingThread.start();


        var pool = Executors.newFixedThreadPool(countThread);
        for (int i = 0; i < countThread; i++) {
            Runnable runnable = () -> {
                String route = generateRoute("RLRFR", 100);
                int countRepeat = countingRepeat(route, 'R');
                synchronized (sizeToFreq) {
                    if (!sizeToFreq.containsKey(countRepeat)) {
                        sizeToFreq.put(countRepeat, 1);
                        try {
                            sizeToFreq.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sizeToFreq.put(countRepeat, sizeToFreq.get(countRepeat) + 1);
                    sizeToFreq.notify();
                }
            };
            pool.submit(runnable);
        }
        pool.shutdown();
        countingThread.interrupt();

        var list = sizeToFreq.entrySet().stream()
                .sorted((e1,e2)->e2.getValue()-e1.getValue())
                .map(x->String.format("-%d (%d раз)", x.getKey(), x.getValue()))
                .collect(Collectors.toList());

        System.out.println("Самое частое количество повторений " + list.get(0) + '\n' +
                "Другие размеры:");
        list.stream().forEach(System.out::println);
    }

    private static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    private static int countingRepeat(String route, char symbol) {
        return (int) route.chars()
                .filter(x -> x == symbol)
                .count();
    }
}









