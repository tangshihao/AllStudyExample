package org.tangshihao.study.zookeeper.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;

/**
 * zookeeper分布式锁应用场景
 */
public class DistributedLockApplication {
    public static void main(String[] args) {
        CuratorFramework cf = CuratorFrameworkFactory.builder().connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        cf.start();
        //基于zookeeper的分布式锁
        final InterProcessMutex lock = new InterProcessMutex(cf, "/lock");
        final CountDownLatch c = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    try {
                        c.await();
                        lock.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(System.currentTimeMillis());
                    try {
                        sleep(1000);
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            c.countDown();
        }
    }
}
