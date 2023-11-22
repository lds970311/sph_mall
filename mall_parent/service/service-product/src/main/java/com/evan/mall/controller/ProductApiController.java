package com.evan.mall.controller;

import com.evan.mall.product.BaseCategoryView;
import com.evan.mall.product.SkuInfo;
import com.evan.mall.service.BaseCategoryViewService;
import com.evan.mall.service.SkuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product/inner")
@Api(tags = "商品详情")
public class ProductApiController {
    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private BaseCategoryViewService baseCategoryViewService;

    @GetMapping("/getSkuInfo/{skuId}")
    @ApiOperation("根据skuId查询sku和图片")
    public SkuInfo getSkuInfo(@PathVariable Long skuId) {
        return skuInfoService.getSkuInfo(skuId);
    }

    @GetMapping("/getCategoryView/{category3Id}")
    @ApiOperation("根据三级分类id获取分类信息")
    public BaseCategoryView findBaseCategory(@PathVariable Long category3Id) {
        return this.baseCategoryViewService.getById(category3Id);
    }

}
