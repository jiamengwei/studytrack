package com.threadpool;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {


    @SneakyThrows
    public static void main(String[] args) {
        Task task = new Task();
        Thread t1 = new Thread(task);
        t1.start();
        Thread t2 = new Thread(task);
        t2.start();
        Thread t3 = new Thread(task);
        t3.start();

        t1.join();
        t2.join();
        t3.join();
        System.out.println(task.number);
        System.out.println(task.lockFailTotal.get());
    }
}

class Task implements Runnable {
    public int number = 0;
    private final Lock lock = new ReentrantLock(true);
    public AtomicInteger lockFailTotal = new AtomicInteger();

    @SneakyThrows
    @Override
    public void run() {
        int index = 0;
        while (index++ < 10000) {
            if (lock.tryLock(2, TimeUnit.SECONDS)) {
                try {
                    number += 1;
                } finally {
                    lock.unlock();
                }
            } else {
                lockFailTotal.incrementAndGet();
            }
        }
    }
}

