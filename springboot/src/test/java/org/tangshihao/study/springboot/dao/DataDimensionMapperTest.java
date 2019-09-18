package org.tangshihao.study.springboot.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.tangshihao.study.springboot.config.ServerConfig;
import org.tangshihao.study.springboot.config.SwaggerConfig;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
// 指定只加载SwaggerConfig这个类
@SpringBootTest(classes = {SwaggerConfig.class})
public class DataDimensionMapperTest {
    @Autowired
    private DataDimensionMapper dataDimensionMapper;

    @Test
    public void test() {
        System.out.println("yes");
        System.out.println("no");
    }
}