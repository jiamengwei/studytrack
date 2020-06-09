package com.lambda.example;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class ReduceDemo {
    public static void main(String[] args) {
        OptionalInt reduced =
            IntStream.range(1, 4).peek(System.out::println).reduce((a, b) -> a + b);
        System.out.println(reduced.getAsInt());

        int reducedTwoParams =
            IntStream.range(1, 4).reduce(10, (a, b) -> a + b);

        System.out.println(reducedTwoParams);

        int reducedParams = Stream.of(1, 2, 3)
            .reduce(10, (a, b) -> a + b, (a, b) -> {
                log.info("combiner was called");
                return a + b;
            });

        System.out.println(reducedParams);

        int reducedParallel = Arrays.asList(1, 2, 3).parallelStream()
            .reduce(10, (a, b) -> {
                log.info("accumulator was called {} , {}", a,b);
                return (a + b);
                }, (a, b) -> {
                log.info("combiner was called {} , {}", a,b);
                return a + b;
            });

        System.out.println(reducedParallel);


    }
}
