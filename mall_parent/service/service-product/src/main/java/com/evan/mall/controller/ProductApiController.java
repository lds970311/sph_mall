package com.evan.mall.controller;

import com.evan.mall.product.*;
import com.evan.mall.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product/inner")
@Api(tags = "商品详情")
public class ProductApiController {
    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private BaseCategoryViewService baseCategoryViewService;

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SpuPosterService spuPosterService;

    @Autowired
    private BaseAttrInfoService baseAttrInfoService;

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

    @GetMapping("/getSkuPrice/{skuId}")
    @ApiOperation("根据skuId查询商品实时价格")
    public BigDecimal getSkuPrice(@PathVariable Long skuId) {
        return this.skuInfoService.getSkuPrice(skuId);
    }

    @GetMapping("/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    @ApiOperation("获取spu销售属性与房钱sku销售属性选中状态")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@PathVariable Long skuId, @PathVariable Long spuId) {
        return this.spuSaleAttrService.getSpuSaleAttrListCheckBySku(skuId, spuId);
    }

    @GetMapping("/getSkuValueIdsMap/{spuId}")
    @ApiOperation("获取每个销售属性值和sku的对应关系，实现sku切换")
    public Map<String, Object> getSkuValueIdsMap(@PathVariable Long spuId) {
        List<Map<String, Object>> skuValueIdsMap = this.skuSaleAttrValueService.getSkuValueIdsMap(spuId);
        Map<String, Object> resultMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(skuValueIdsMap)) {
            for (Map<String, Object> map : skuValueIdsMap) {
                resultMap.put((String) map.get("value_ids"), map.get("sku_id"));
            }
        }
        return resultMap;
    }

    @GetMapping("/findPosterBySpuId/{spuId}")
    @ApiOperation("根据spuId获取海报信息")
    public List<SpuPoster> findPosterBySpuId(@PathVariable Long spuId) {
        return this.spuPosterService.findPosterBySpuId(spuId);
    }

    @GetMapping("/getAttrList/{skuId}")
    @ApiOperation("根据skuId获取平台属性")
    public List<BaseAttrInfo> getAttrList(@PathVariable Long skuId) {
        return this.baseAttrInfoService.getAttrListBySkuId(skuId);

    }
}
