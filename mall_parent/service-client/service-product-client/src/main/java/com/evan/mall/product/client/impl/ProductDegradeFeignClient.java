package com.evan.mall.product.client.impl;

import com.alibaba.fastjson.JSONObject;
import com.evan.mall.common.result.Result;
import com.evan.mall.product.*;
import com.evan.mall.product.client.ProductFeignClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @ClassName ProductDegradeFeignClient
 * @Author Evan
 * @date 2023/11/25
 * 实现服务降级
 */
@Component
public class ProductDegradeFeignClient implements ProductFeignClient {
    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        return null;
    }

    @Override
    public BaseCategoryView findBaseCategory(Long category3Id) {
        return null;
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return null;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return null;
    }

    @Override
    public Map<String, Object> getSkuValueIdsMap(Long spuId) {
        return null;
    }

    @Override
    public List<SpuPoster> findPosterBySpuId(Long spuId) {
        return null;
    }

    @Override
    public List<BaseAttrInfo> getAttrList(Long skuId) {
        return null;
    }

    @Override
    public Result<List<JSONObject>> getBaseCategoryList() {
        return null;
    }
}
