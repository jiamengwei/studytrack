package com.concurrent.exercise;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CompletableFutureExample {
    @SneakyThrows
    public static void main(String[] args) {
        //or  任一任务执行完成即可返回结果
        applyToEither();
        //and  任务都执行完才会返回结果
        thenCombine();
    }

    @SneakyThrows
    static void thenCombine() {
        CompletableFuture<Void> f1 =
            CompletableFuture.runAsync(() -> {
                log.info("hello");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            });

        CompletableFuture<String> f2 =
            CompletableFuture.supplyAsync(() -> "world");

        //and
        CompletableFuture<String> f3 =
            f1.thenCombine(f2, (a, b) -> {
                log.info("f1执行结果：{}", a);
                log.info("f2执行结果：{}", b);
                return b + " and " + a;
            });
        String f3Result = f3.get();
        log.info(f3Result);
    }

    @SneakyThrows
    static void applyToEither() {
        CompletableFuture<String> f1 =
            CompletableFuture.supplyAsync(() -> {
                log.info("hello");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
                return "hello";
            });

        CompletableFuture<String> f2 =
            CompletableFuture.supplyAsync(() -> "world");

        //or
        CompletableFuture<String> f3 =
            f1.applyToEither(f2, a -> {
                log.info("f1执行结果：{}", a);
                return a;
            });

        String f3Result = f3.get();
        log.info(f3Result);


    }
}


