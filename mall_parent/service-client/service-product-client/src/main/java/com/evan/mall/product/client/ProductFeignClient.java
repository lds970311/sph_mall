package com.evan.mall.product.client;


import com.alibaba.fastjson.JSONObject;
import com.evan.mall.common.result.Result;
import com.evan.mall.product.*;
import com.evan.mall.product.client.impl.ProductDegradeFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(value = "service-product", fallback = ProductDegradeFeignClient.class)
public interface ProductFeignClient {

    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    SkuInfo getSkuInfo(@PathVariable Long skuId);


    @GetMapping("/api/product/inner/getCategoryView/{category3Id}")
    BaseCategoryView findBaseCategory(@PathVariable Long category3Id);

    @GetMapping("/api/product/inner/getSkuPrice/{skuId}")
    BigDecimal getSkuPrice(@PathVariable Long skuId);


    @GetMapping("/api/product/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@PathVariable Long skuId, @PathVariable Long spuId);

    @GetMapping("/api/product/inner/getSkuValueIdsMap/{spuId}")
    Map<String, Object> getSkuValueIdsMap(@PathVariable Long spuId);

    @GetMapping("/api/product/inner/findPosterBySpuId/{spuId}")
    List<SpuPoster> findPosterBySpuId(@PathVariable Long spuId);

    @GetMapping("/api/product/inner/getAttrList/{skuId}")
    List<BaseAttrInfo> getAttrList(@PathVariable Long skuId);

    @GetMapping("/api/product/getBaseCategoryList")
    Result<List<JSONObject>> getBaseCategoryList();

    @GetMapping("/api/product/inner/getTrademark/{tmId}")
    @ApiOperation("根据tmId获取品牌信息")
    BaseTrademark getTrademark(@PathVariable Long tmId);
}