package org.tangshihao.study.test.examples;

import org.easymock.EasyMock;
import org.easymock.internal.MocksControl;

public class MockUse {
    public static void main(String[] args) {
        TestInterface ti = EasyMock.createMock(TestInterface.class);
        EasyMock.expect(ti.test("1")).andReturn("1a");
        EasyMock.expect(ti.test("2")).andReturn("2a");
        EasyMock.expect(ti.test("3")).andReturn("3a");
        //录制，让以上列出的期望条件都实现
        EasyMock.replay(ti);
        System.out.println(ti.test("1"));
    }
}
