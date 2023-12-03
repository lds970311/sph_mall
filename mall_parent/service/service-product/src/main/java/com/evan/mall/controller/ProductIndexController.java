package com.evan.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.evan.mall.common.result.Result;
import com.evan.mall.service.BaseCategoryViewService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "商城首页数据接口")
@RequestMapping("/api/product")
public class ProductIndexController {
    @Autowired
    private BaseCategoryViewService baseCategoryViewService;

    /**
     * 获取全部分类信息
     *
     * @return
     */
    @GetMapping("/getBaseCategoryList")
    public Result<List<JSONObject>> getBaseCategoryList() {
        List<JSONObject> list = this.baseCategoryViewService.getBaseCategoryList();
        return Result.ok(list);
    }
}
