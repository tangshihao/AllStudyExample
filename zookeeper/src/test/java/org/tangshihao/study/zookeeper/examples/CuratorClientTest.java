package org.tangshihao.study.zookeeper.examples;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorClientTest {
    private CuratorClient cc = null;

    @Before
    public void before() {
        cc = new CuratorClient();
        cc.init();
    }

    @After
    public void after() {
        cc.close();
    }

    @Test
    public void create() throws Exception {
        cc.create("/test1", "123");
    }

    @Test
    public void create2() throws Exception {
        cc.createEphemeral("/test1", "222");
    }

    @Test
    public void getData() throws Exception {
        System.out.println(cc.getData("/test1"));
    }

    @Test
    public void createNodeAsyn() throws Exception {
        cc.createNodeAsyn("/test2", "333");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void getDataWithListen() throws Exception {
        System.out.println(cc.getDataWithListen("/test1"));
        Thread.sleep(Integer.MAX_VALUE);
    }
}
