package org.tangshihao.study.zookeeper.examples;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 与zookeeper集群创建会话连接
 */
public class ZookeeperSession implements Watcher {
    private static CountDownLatch sema = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        //zookeeper的连接是一个异步过程，创建连接后实际上并没有立即建立连接
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000,
                new ZookeeperSession());
        System.out.println(zooKeeper.getState());
        sema.await();
        //指定sessionId，则可以复用上一次未关闭的会话
        long sessionId = zooKeeper.getSessionId();
        byte[] password = zooKeeper.getSessionPasswd();
        zooKeeper = new ZooKeeper("localhost:2181", 5000, new ZookeeperSession(),
                sessionId, password);
        Thread.sleep(Integer.MAX_VALUE);
    }


    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            sema.countDown();
        }
    }
}
