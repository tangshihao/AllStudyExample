package org.tangshihao.study.zookeeper.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DistributedBarrierApplication {
    public static void main(String[] args) throws Exception {
        CuratorFramework cf = CuratorFrameworkFactory.builder().connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        cf.start();
        final DistributedBarrier db = new DistributedBarrier(cf, "/barrier");
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    System.out.println("集合");
                    try {
                        db.setBarrier();
                        db.waitOnBarrier();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("开始执行");
                }
            }.start();
        }
        Thread.sleep(3000);
        db.removeBarrier();
    }
}
