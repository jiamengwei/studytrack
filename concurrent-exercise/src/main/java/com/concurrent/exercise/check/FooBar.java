package com.concurrent.exercise.check;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
class FooBar {
    private int n;
    AtomicBoolean flag = new AtomicBoolean(false);

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            if (!flag.get()) {
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                flag.set(true);
            } else {
                --i;
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            if (flag.get()) {
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                flag.set(false);
            } else {
                --i;
            }
        }
    }

    public static void main(String[] args) {
        FooBar bar = new FooBar(5000000);
        Runnable fooR = () -> {
            log.info("foo");
        };
        Runnable barR = () -> {
            log.info("bar");
        };
        new Thread(() -> {
            try {
                bar.foo(fooR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                bar.bar(barR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}