package org.tangshihao.study.springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value="测试模块")
@RestController
@RequestMapping("test")
//服务启动后访问页面url:port/swagger-ui.html就能访问到接口定义文档
public class TestController {
    @ApiOperation(value = "测试接口1", notes = "没有参数")
    @GetMapping("test1")
    public String test1() {
        return "successfully";
    }

    @ApiOperation(value = "测试接口2", notes = "有参数")
    @GetMapping("test2")
    public String test2(@RequestParam("content") String content) {
        return content;
    }
}
