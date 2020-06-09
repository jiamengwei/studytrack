package com.threadpool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolDemo {

    private static final AtomicInteger threadNumber = new AtomicInteger(1);



    public static void main(String[] args) {
        BlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>(5);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(4,
            4,
            5,
            TimeUnit.SECONDS,
            linkedBlockingQueue,
            new MyThreadFactory("hw"),
            new ThreadPoolExecutor.AbortPolicy()
        );
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        Task task4 = new Task();

        Future<List<User>> submit1 = executor.submit(task1);
        Future<List<User>> submit2 = executor.submit(task2);
        Future<List<User>> submit3 = executor.submit(task3);
        Future<List<User>> submit4 = executor.submit(task4);

        try {
            submit2.get(1,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println();

        try {
            System.out.println(submit1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        executor.shutdown();
    }

    private static class Task implements Callable<List<User>> {

        @SneakyThrows
        @Override
        public List<User> call()  {
            TimeUnit.SECONDS.sleep(4);
            return Arrays.asList(new User(1,Thread.currentThread().getName()));
        }
    }
    private static class MyThreadFactory implements ThreadFactory {
        private final String namePrefix;
        public MyThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, namePrefix + "-" + threadNumber.getAndIncrement());
        }
    }

    @Data
    @AllArgsConstructor
    public static class User {
        private Integer id;
        private String name;
    }
}

