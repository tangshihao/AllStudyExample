package org.tangshihao.study.springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;


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

    @ApiOperation(value = "测试接口3", notes = "session参数")
    @GetMapping("test3")
    public void test3(HttpServletRequest req) {
        // 可以获取请求头等信息，还有session等信息
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ":" + req.getHeader(headerName));
        }
    }

    @ApiOperation(value = "测试接口4", notes = "测试session")
    @GetMapping("test4")
    public void test4(HttpServletRequest req) {
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("name", "lilee");
    }

    @ApiOperation(value = "测试接口5", notes = "测试session")
    @GetMapping("test5")
    public String test5(HttpServletRequest req) {
        return (String)req.getSession().getAttribute("name");
    }
}
