package com.concurrent.exercise.check;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadPool {

    @SneakyThrows
    public static void main(String[] args) {

        BlockingQueue workQueue = new LinkedBlockingQueue();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1,
            3,
            2,
            TimeUnit.SECONDS,
            workQueue);

        User user = new User("Paul");

        Future<User> submit = executor.submit(new Task(user), user);
        Object result = submit.get();
        System.out.println("result:" + result);

        executor.shutdown();
    }
}


class Task implements Runnable {

    private User user;

    public Task(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        System.out.println(user);
        user.setUsername("John");
    }
}

@Data
@AllArgsConstructor
class User {
    private String Username;
}