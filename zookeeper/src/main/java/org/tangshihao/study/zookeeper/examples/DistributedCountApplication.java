package org.tangshihao.study.zookeeper.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.CountDownLatch;

public class DistributedCountApplication {
    public static void main(String[] args) throws Exception {
        CuratorFramework cf = CuratorFrameworkFactory.builder().connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        cf.start();
        //分布式计数器是根据节点"/count"的version来计算的，因此是可以保存下来的
        final DistributedAtomicInteger count = new DistributedAtomicInteger(cf, "/count", new RetryNTimes(1000, 3));
        final CountDownLatch c = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    try {
                        c.await();
                        count.increment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        c.countDown();
        Thread.sleep(5000);
        System.out.println(count.get().postValue());
    }
}
