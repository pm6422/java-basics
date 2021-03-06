package org.infinity.javabasics.concurrency;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchSample1 {

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(2);

        new Thread() {
            public void run() {
                try {
                    System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
                    Thread.sleep(3000);
                    System.out.println("子线程" + Thread.currentThread().getName() + "运行中");
                    latch.countDown();
                    System.out.println("子线程" + Thread.currentThread().getName() + "还在运行中");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();

        new Thread() {
            public void run() {
                try {
                    System.out.println("子线程" + Thread.currentThread().getName() + "正在执行");
                    Thread.sleep(5000);
                    System.out.println("子线程" + Thread.currentThread().getName() + "运行中");
                    latch.countDown();
                    System.out.println("子线程" + Thread.currentThread().getName() + "还在运行中");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();

        try {
            System.out.println("等待2个子线程执行完毕...");
            latch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

