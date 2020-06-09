package com.concurrent.exercise.check;

import lombok.SneakyThrows;

import java.util.Vector;
import java.util.concurrent.*;

public class CyclicBarrierDemo {

    Runnable barrierAction = new Runnable() {
        @Override
        public void run() {
            System.out.println("running");
        }
    };

    CyclicBarrier cyclicBarrier = new CyclicBarrier(2, barrierAction);


    @SneakyThrows
    public   void reduce(){
        cyclicBarrier.await();
    }

    public static void main(String[] args) {
        CyclicBarrierDemo demo = new CyclicBarrierDemo();
        new Thread(()->demo.reduce()).start();
        new Thread(()->demo.reduce()).start();
    }
}


