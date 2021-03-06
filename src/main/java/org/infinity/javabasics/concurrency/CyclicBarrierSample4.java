package org.infinity.javabasics.concurrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

// 与闭锁结构一致
public class CyclicBarrierSample4 {
    public static void main(String[] args) throws InterruptedException {
        Runnable taskTemp = new Runnable() {
            private int iCounter;
            @Override
            public void run() {
                // 发起请求
                // HttpClientOp.doGet("https://www.baidu.com/");
                iCounter++;
                System.out.println(System.nanoTime() + " [" + Thread.currentThread().getName() + "] iCounter = " + iCounter);
            }
        };
        CyclicBarrierSample4 latchTest = new CyclicBarrierSample4();
        // latchTest.startTaskAllInOnce(5, taskTemp);
        latchTest.startNThreadsByBarrier(5, taskTemp);
    }

    public void startNThreadsByBarrier(int threadNum, Runnable finishTask) throws InterruptedException {
        // 启动threadNum个线程，与栅栏阀值一致，即当线程准备数达到要求时，栅栏便会打开，从而达到统一控制效果
        // finishTask设置栅栏打开前的操作，比如初始化某些值
        CyclicBarrier barrier = new CyclicBarrier(threadNum, finishTask);
        for (int i = 0; i < threadNum; i++) {
            Thread.sleep(100);
            new Thread(new CounterTask(barrier)).start();
        }
        System.out.println(Thread.currentThread().getName() + " out over...");
    }
}

class CounterTask implements Runnable {

    // 传入栅栏，一般考虑更优雅方式
    private CyclicBarrier barrier;

    public CounterTask(final CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " - " + System.currentTimeMillis() + " is ready...");
        try {
            // 设置栅栏，使在此等待，到达位置的线程达到要求即可开启大门
            // 其实可以使用thread.join()代替

            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " - " + System.currentTimeMillis() + " started...");
    }
}