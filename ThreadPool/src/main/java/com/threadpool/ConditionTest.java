package com.threadpool;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class ConditionTest {
    @SneakyThrows
    public static void main(String[] args) {

        BoundedBuffer boundedBuffer = new BoundedBuffer();
        ListPutTask putTask = new ListPutTask(boundedBuffer);
        ListTakeTask takeTask = new ListTakeTask(boundedBuffer);
        Thread t1 = new Thread(putTask);
        Thread t2 = new Thread(putTask);

        Thread t3 = new Thread(takeTask);
        Thread t4 = new Thread(takeTask);
        Thread t5 = new Thread(takeTask);

        t1.start();
        t2.start();

        TimeUnit.MILLISECONDS.sleep(1);

        t3.start();
        t4.start();
        t5.start();
    }
}

class ListTakeTask implements Runnable {

    private BoundedBuffer boundedBuffer;

    public ListTakeTask(BoundedBuffer boundedBuffer) {
        this.boundedBuffer = boundedBuffer;
    }

    @SneakyThrows
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            boundedBuffer.take();
        }
    }
}

class ListPutTask implements Runnable {

    private BoundedBuffer boundedBuffer;

    public ListPutTask(BoundedBuffer boundedBuffer) {
        this.boundedBuffer = boundedBuffer;
    }

    @SneakyThrows
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            boundedBuffer.put(1);
        }
    }
}