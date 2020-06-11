package com.concurrent.exercise;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CompletionServiceExample {
    @SneakyThrows
    public static void main(String[] args) {
        Executor executor = Executors.newFixedThreadPool(2);
        CompletionService completionService = new ExecutorCompletionService<String>(executor);
        completionService.submit(()-> "1");
        completionService.submit(()-> "2");
        completionService.submit(()->{TimeUnit.SECONDS.sleep(2);return "3";});
        completionService.submit(()-> "4");

        for (int i = 0; i < 4; i++) {
            Future res = completionService.take();
            System.out.println(res.get());
        }
    }
}
