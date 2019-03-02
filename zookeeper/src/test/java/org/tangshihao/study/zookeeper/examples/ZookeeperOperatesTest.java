package org.tangshihao.study.zookeeper.examples;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ZookeeperOperatesTest {
    private ZookeeperOperates zo = null;

    @Before
    public void init() throws IOException, InterruptedException {
        zo = new ZookeeperOperates();
        zo.init();
    }

    @After
    public void close() throws InterruptedException {
        if (zo != null) {
            zo.close();
        }
    }

    @Test
    public void testCreateNode() throws KeeperException, InterruptedException {
        zo.createNode("/test1");
    }

    @Test
    public void testCreateNodeAsyn() {
        zo.createNodeAsyn("/test1/test2");
    }
}
