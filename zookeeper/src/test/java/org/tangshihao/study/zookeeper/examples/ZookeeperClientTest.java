package org.tangshihao.study.zookeeper.examples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;

public class ZookeeperClientTest {
    private ZookeeperClient zc = null;

    @Before
    public void init() {
        zc = new ZookeeperClient();
        zc.init();
    }

    @Test
    public void testSubcribeChild() throws InterruptedException {
        zc.subChild("/test1");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testSetData() {
        zc.setData("/zk-book", "234");
    }

    @Test
    public void testSubcribeData() throws InterruptedException {
        zc.subData("/zk-book");
        Thread.sleep(5000);
        zc.setData("/zk-book", "1111");
        System.out.println("节点已经被更新");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @After
    public void close() {
        zc.close();
    }
}
