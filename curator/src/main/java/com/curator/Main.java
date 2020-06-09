package com.curator;

import lombok.SneakyThrows;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class Main {

    private static final String LOCK_PATH = "/examples/locks";
    static InterProcessMutex lock = null;

    @SneakyThrows
    public static void main(String[] args) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();
        lock = new InterProcessMutex(client, LOCK_PATH);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                something();
            }
        };

        for (int i = 0; i < 2000; i++) {
            new Thread(runnable).start();
        }
    }

    @SneakyThrows
    public static void something() {

        if (lock.acquire(3, TimeUnit.SECONDS)) {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println(Thread.currentThread().getName() + " has the lock");
                System.out.println(Thread.currentThread().getName() + " do something");
            } finally {
                lock.release();
                System.out.println(Thread.currentThread().getName() + " release the lock");

            }
        }
    }
}
