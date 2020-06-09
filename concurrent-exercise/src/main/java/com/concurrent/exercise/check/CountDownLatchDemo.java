package com.concurrent.exercise.check;

import lombok.SneakyThrows;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CountDownLatchDemo {

    Vector<Integer> pos = new Vector<>();
    Vector<Integer> dos = new Vector<>();

    @SneakyThrows
    private void getPOrders() {
//        Random random = new Random();
//        int i = random.nextInt(17);
        TimeUnit.MILLISECONDS.sleep(500);
        pos.add(1);
    }

    @SneakyThrows
    private void getDOrders() {
//        Random random = new Random();
//        int i = random.nextInt(2);
        TimeUnit.MILLISECONDS.sleep(200);
        dos.add(1);
    }

    private void check() {
        if (pos.size() != dos.size()) {
            throw new IllegalStateException("error");
        }

        if (!pos.containsAll(dos)) {
            throw new IllegalStateException("error");
        }

    }

     void doWorkA() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            getPOrders();
            getDOrders();
            check();
        }
        long end = System.currentTimeMillis();
        System.out.println("common:" + (end - start));
    }

    @SneakyThrows
    void doWorkB() {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            CountDownLatch countDownLatch = new CountDownLatch(2);
            executorService.execute(() -> {
                getPOrders();
                countDownLatch.countDown();
            });
            executorService.execute(() -> {
                getDOrders();
                countDownLatch.countDown();
            });
            countDownLatch.await();
            check();
        }
        AtomicInteger integer = new AtomicInteger();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        executorService.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("countDownLatch:" + (end - start));
    }

    public static void main(String[] args) {
        CountDownLatchDemo demo = new CountDownLatchDemo();
        demo.doWorkA();
        demo.doWorkB();
    }
}


