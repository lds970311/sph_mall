package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.service.SkuItemApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/item")
@Api("sku详情")
public class SkuItemApiController {
    @Autowired
    private SkuItemApiService skuItemApiService;

    @GetMapping("/{skuId}")
    @ApiOperation("根据skuId查询item")
    public Result<Map<String, Object>> getSkuItem(@PathVariable Long skuId) {
        Map<String, Object> res = skuItemApiService.getSkuItemBySkuId(skuId);
        return Result.ok(res);
    }


}
