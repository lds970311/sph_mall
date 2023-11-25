package com.evan.mall.service.impl;

import com.evan.mall.product.*;
import com.evan.mall.product.client.ProductFeignClient;
import com.evan.mall.service.SkuItemApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkuItemApiServiceImpl implements SkuItemApiService {

    @Qualifier("com.evan.mall.product.client.ProductFeignClient")
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public Map<String, Object> getSkuItemBySkuId(Long skuId) {
        SkuInfo skuInfo = this.productFeignClient.getSkuInfo(skuId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("skuInfo", skuInfo);
        if (null != skuInfo) {
            //获取三级分类
            BaseCategoryView baseCategory = this.productFeignClient.findBaseCategory(skuInfo.getCategory3Id());
            resultMap.put("categoryView", baseCategory);
            //获取销售属性和选中状态
            List<SpuSaleAttr> saleAttrListCheckBySku = this.productFeignClient.getSpuSaleAttrListCheckBySku(skuId, skuInfo.getSpuId());
            resultMap.put("spuSaleAttrList", saleAttrListCheckBySku);
            //获取商品切换数据
            Map<String, Object> skuValueIdsMap = this.productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
            resultMap.put("valuesSkuJson", skuValueIdsMap);
            //获取海报信息
            List<SpuPoster> posterList = this.productFeignClient.findPosterBySpuId(skuInfo.getSpuId());
            resultMap.put("spuPosterList", posterList);
        }
        //获取实时价格
        BigDecimal skuPrice = this.productFeignClient.getSkuPrice(skuId);
        resultMap.put("price", skuPrice);

        //获取平台信息
        List<BaseAttrInfo> attrList = this.productFeignClient.getAttrList(skuId);
        resultMap.put("skuAttrList", attrList);

        return resultMap;
    }
}
