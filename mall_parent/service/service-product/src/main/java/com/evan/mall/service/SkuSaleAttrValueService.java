package com.evan.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.SkuSaleAttrValue;

import java.util.List;
import java.util.Map;

/**
 * @author 26966
 * @description 针对表【sku_sale_attr_value(sku销售属性值)】的数据库操作Service
 * @createDate 2023-11-18 00:04:27
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {

    List<Map<String, Object>> getSkuValueIdsMap(Long spuId);
}
