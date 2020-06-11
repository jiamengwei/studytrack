package com.concurrent.exercise;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class SemaphoreExample {

    @SneakyThrows
    public static void main(String[] args) {
        //每次最多通过3辆车
        RedAndGreenLight light = new RedAndGreenLight(4);

        Runnable car = () -> light.car();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            new Thread(car).start();
        });
    }
}

/**
 * 限制通过车辆数的红绿灯
 */
@Slf4j
class RedAndGreenLight {
    private Semaphore semaphore;

    /**
     * @param permits 允许通过的车辆数
     */
    public RedAndGreenLight(int permits) {
        semaphore = new Semaphore(permits);
    }

    /**
     * 通过红绿灯
     */
    @SneakyThrows
    public void car() {
        semaphore.acquireUninterruptibly(2);
//        semaphore.acquire();
        try {
            log.info("{}", semaphore.getQueueLength());
            TimeUnit.SECONDS.sleep(2);
        } finally {
            semaphore.release();
        }
    }
}


