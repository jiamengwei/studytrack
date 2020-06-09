package com.lambda.example;

import sun.net.www.http.HttpClient;

import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class StreamOfPrimitives {
    public static void main(String[] args) {

        /**
         * range(int startInclusive, int endExclusive)
         * 根据指定范围生成一个有序的IntStream
         * 范围：从 startInclusive 到 endExclusive，不包含endExclusive
         */
        IntStream intStream = IntStream.range(1,3);
        intStream.forEach(System.out::println);
        /**
         * 输出结果：
         * 1
         * 2
         */

        /**
         * rangeClosed(long startInclusive, final long endInclusive)
         * 根据指定范围生成一个有序的LongStream
         * 范围：从 startInclusive 到 endInclusive，包含endInclusive
         */
        LongStream longStream = LongStream.rangeClosed(1L, 3L);
        longStream.forEach(System.out::println);
        /**
         * 输出结果：
         * 1
         * 2
         * 3
         */

        Random random = new Random();
        /**
         * doubles(long streamSize)
         * 生成一个包含3个随机元素的DoubleStream
         */
        DoubleStream doubleStreamA = random.doubles(3);
        doubleStreamA.forEach(System.out::println);
        /**
         * 输出结果：
         * 0.07813075638804146
         * 0.9548541514464832
         * 0.5852433587390855
         */

        /**
         * doubles(long streamSize, double randomNumberOrigin, double randomNumberBound)
         * 根据指定范围生成一个包含3个元素的DoubleStream
         * 范围:从 randomNumberOrigin 到 randomNumberBound, 不包括randomNumberBound
         */
        DoubleStream doubleStreamB = random.doubles(3, 1L,3L);
        doubleStreamB.forEach(System.out::println);
        /**
         * 输出结果：
         * 1.892529444053839
         * 2.3450260568772463
         * 1.3879168598931126
         */
    }
}
