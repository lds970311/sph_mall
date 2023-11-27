package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.service.TestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "测试接口")
@RequestMapping("admin/product/test")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/testLock")
    public Result testLock() {
        try {
            testService.testLock();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Result.ok();
    }
}
