package com.evan.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.evan.mall.common.result.Result;
import com.evan.mall.product.client.ProductFeignClient;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Controller
@Api(tags = "商城首页")
public class IndexController {
    @Qualifier("com.evan.mall.product.client.ProductFeignClient")
    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping({"/", "/index.html"})
    public String index(Model model) {
        Result<List<JSONObject>> result = this.productFeignClient.getBaseCategoryList();
        List<JSONObject> data = result.getData();
        model.addAttribute("list", data);
        return "index/index";
    }

    /**
     * 生成页面，Nginx做静态代理
     *
     * @return
     */
    @GetMapping("/createIndex")
    @ResponseBody
    public Result<Void> createIndex(HttpServletRequest request) throws IOException {
        //生成页面
        Result<List<JSONObject>> baseCategoryList = this.productFeignClient.getBaseCategoryList();
        //创建对象封装数据
        Context context = new Context();
        context.setVariable("list", baseCategoryList.getData());
        //httpServlet获取当前文件夹路径
        String path = "D:\\pages\\html\\";
        FileWriter fileWriter = new FileWriter(path);
        this.templateEngine.process("index/index.html", context, fileWriter);
        return Result.ok();
    }
}
