package org.tangshihao.study.zookeeper.examples;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

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

    @Test
    public void testDeleteNode() throws KeeperException, InterruptedException {
        zo.deleteNode("/test1", 0);
    }

    @Test
    public void testDeleteNodeAsyn() {
        zo.deleteNodeAsyn("/test1/test2", 0);
    }

    @Test
    public void testGetData() throws KeeperException, InterruptedException {
        System.out.println(zo.getData("/test1", true));
    }

    @Test
    public void testGetDataAsyn() throws KeeperException, InterruptedException {
        zo.getDataAsyn("/test1", true);
    }

    @Test
    public void testGetData2() throws KeeperException, InterruptedException {
        System.out.println(zo.getData("/test1", true));
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testGetChildren() throws KeeperException, InterruptedException {
        List<String> result = zo.getChildren("/test1", true);
        System.out.println(ZookeeperOperates.print(result));
    }

    @Test
    public void testGetChildrenAsyn() {
        zo.getChildrenAsyn("/test1", true);
    }

    @Test
    public void testGetChildren2() throws KeeperException, InterruptedException {
        List<String> result = zo.getChildren("/test1", true);
        System.out.println(ZookeeperOperates.print(result));
        Thread.sleep(Integer.MAX_VALUE);
    }
}
