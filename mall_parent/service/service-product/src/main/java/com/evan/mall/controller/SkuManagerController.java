package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.product.SkuInfo;
import com.evan.mall.product.SpuImage;
import com.evan.mall.product.SpuSaleAttr;
import com.evan.mall.service.SkuInfoService;
import com.evan.mall.service.SpuImageService;
import com.evan.mall.service.SpuSaleAttrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/product")
@Api("sku属性操作")
public class SkuManagerController {
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @Autowired
    private SpuImageService spuImageService;

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/spuSaleAttrList/{spuId}")
    @ApiOperation("根据spuid查询销售属性与销售属性值")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrList(@PathVariable Long spuId) {
        List<SpuSaleAttr> spuSaleAttrList = this.spuSaleAttrService.findSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }

    @GetMapping("/spuImageList/{spuId}")
    @ApiOperation("根据spuId查找图片")
    public Result<List<SpuImage>> getSpuImageList(@PathVariable Long spuId) {

        List<SpuImage> spuImageList = this.spuImageService.getSpuImageListBySpuId(spuId);
        return Result.ok(spuImageList);
    }

    @PostMapping("/saveSkuInfo")
    @ApiOperation("保存sku信息")
    public Result<String> saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        boolean saved = this.skuInfoService.saveSkuInfo(skuInfo);
        return saved ? Result.ok() : Result.fail();
    }

    @GetMapping("/list/{pageNo}/{limit}")
    @ApiOperation("分页查询sku信息")
    public Result<Map<String, Object>> getSkuInfo(@PathVariable Long limit, @PathVariable Long pageNo) {
        Map<String, Object> skuInfoMap = this.skuInfoService.getSkuInfoByPage(pageNo, limit);
        return Result.ok(skuInfoMap);
    }

    @GetMapping("onSale/{skuId}")
    @ApiOperation("上架sku")
    public Result<String> onSale(@PathVariable Long skuId) {
        boolean updated = this.skuInfoService.onSale(skuId);
        return updated ? Result.ok() : Result.fail();
    }

    @GetMapping("cancelSale/{skuId}")
    @ApiOperation("下架sku")
    public Result<String> cancelSale(@PathVariable Long skuId) {
        boolean updated = this.skuInfoService.onSale(skuId);
        return updated ? Result.ok() : Result.fail();
    }
}

