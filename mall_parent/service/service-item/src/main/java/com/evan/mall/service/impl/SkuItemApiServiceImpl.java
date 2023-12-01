package com.evan.mall.service.impl;

import com.evan.mall.common.constant.RedisConst;
import com.evan.mall.product.*;
import com.evan.mall.product.client.ProductFeignClient;
import com.evan.mall.service.SkuItemApiService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class SkuItemApiServiceImpl implements SkuItemApiService {
    @Autowired
    private RedissonClient redissonClient;

    @Qualifier("com.evan.mall.product.client.ProductFeignClient")
    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public Map<String, Object> getSkuItemBySkuId(Long skuId) {
        Map<String, Object> resultMap = new HashMap<>();
        //使用布隆过滤器，判断redis中是否有该skuId
        RBloomFilter<Object> bloomFilter = this.redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);
        boolean contains = bloomFilter.contains(skuId);
        if (!contains) {
            //如果布隆过滤器中没有该skuId，直接返回空结果
            return resultMap;
        }
        SkuInfo skuInfo = this.productFeignClient.getSkuInfo(skuId);

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

    /**
     * 异步获取sku详情
     *
     * @param skuId
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> getSkuItemBySkuIdAsync(Long skuId) {
        Map<String, Object> resultMap = new HashMap<>();
        //使用布隆过滤器，判断redis中是否有该skuId
        RBloomFilter<Object> bloomFilter = this.redissonClient.getBloomFilter(RedisConst.SKU_BLOOM_FILTER);
        boolean contains = bloomFilter.contains(skuId);
        if (!contains) {
            //如果布隆过滤器中没有该skuId，直接返回空结果
            return resultMap;
        }

        CompletableFuture<SkuInfo> skuInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfo skuInfo = this.productFeignClient.getSkuInfo(skuId);
            resultMap.put("skuInfo", skuInfo);
            return skuInfo;
        }, this.threadPoolExecutor);

        CompletableFuture<Void> findBaseCategoryCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync((skuInfo) -> {
            if (null != skuInfo) {
                //获取三级分类
                BaseCategoryView baseCategory = this.productFeignClient.findBaseCategory(skuInfo.getCategory3Id());
                resultMap.put("categoryView", baseCategory);
            }
        }, this.threadPoolExecutor);

        CompletableFuture<Void> getSpuSaleAttrListCheckBySkuCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync((skuInfo) -> {
            if (null != skuInfo) {
                //获取销售属性和选中状态
                List<SpuSaleAttr> saleAttrListCheckBySku = this.productFeignClient.getSpuSaleAttrListCheckBySku(skuId, skuInfo.getSpuId());
                resultMap.put("spuSaleAttrList", saleAttrListCheckBySku);
            }
        }, this.threadPoolExecutor);

        CompletableFuture<Void> getSkuValueIdsMapCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync((skuInfo) -> {
            if (null != skuInfo) {
                //获取商品切换数据
                Map<String, Object> skuValueIdsMap = this.productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
                resultMap.put("valuesSkuJson", skuValueIdsMap);
            }
        }, this.threadPoolExecutor);

        CompletableFuture<Void> findPosterBySpuIdCompletableFuture = skuInfoCompletableFuture.thenAcceptAsync((skuInfo) -> {
            if (null != skuInfo) {
                //获取海报信息
                List<SpuPoster> posterList = this.productFeignClient.findPosterBySpuId(skuInfo.getSpuId());
                resultMap.put("spuPosterList", posterList);
            }
        }, this.threadPoolExecutor);


        CompletableFuture<Void> skuPriceTask = CompletableFuture.runAsync(() -> {
            BigDecimal skuPrice = this.productFeignClient.getSkuPrice(skuId);
            resultMap.put("price", skuPrice);
        }, this.threadPoolExecutor);

        CompletableFuture<Void> getAttrListCompletableFuture = CompletableFuture.runAsync(() -> {
            List<BaseAttrInfo> attrList = this.productFeignClient.getAttrList(skuId);
            resultMap.put("skuAttrList", attrList);
        }, this.threadPoolExecutor);

        //多任务组合
        CompletableFuture.allOf(skuInfoCompletableFuture,
                findBaseCategoryCompletableFuture,
                getSpuSaleAttrListCheckBySkuCompletableFuture,
                getSkuValueIdsMapCompletableFuture,
                findPosterBySpuIdCompletableFuture,
                skuPriceTask,
                getAttrListCompletableFuture).join();

        return resultMap;
    }

}
