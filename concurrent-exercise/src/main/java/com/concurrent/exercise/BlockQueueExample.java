package com.concurrent.exercise;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockQueueExample {
    public static void main(String[] args) {
        BlockingQueue q = new LinkedBlockingQueue<>();
        Producer p = new Producer(q);
        Consumer c1 = new Consumer(q);
        Consumer c2 = new Consumer(q);

        new Thread(p).start();
        new Thread(c1).start();
        new Thread(c2).start();
    }
}

@Slf4j
class Producer implements Runnable {
    private final BlockingQueue queue;
    private AtomicInteger count;

    Producer(BlockingQueue q) {
        count = new AtomicInteger(0);
        queue = q;
    }

    public void run() {
        try {
            while (true) {
                log.info("开始生产");
                TimeUnit.SECONDS.sleep(6);
                queue.put(produce());
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    Object produce() {
        log.info("生产成功");
        return count.getAndIncrement();
    }
}

@Slf4j
class Consumer implements Runnable {
    private final BlockingQueue queue;

    Consumer(BlockingQueue q) {
        queue = q;
    }

    public void run() {
        try {
            while (true) {
                log.info("开始消费");
                consume(queue.take());
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    void consume(Object x) {
        log.info("消费成功:{}", x);
    }
}
