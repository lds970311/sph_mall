package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.common.cache.RedisCache;
import com.evan.mall.mapper.SkuSaleAttrValueMapper;
import com.evan.mall.product.SkuSaleAttrValue;
import com.evan.mall.service.SkuSaleAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 26966
 * @description 针对表【sku_sale_attr_value(sku销售属性值)】的数据库操作Service实现
 * @createDate 2023-11-18 00:04:27
 */
@Service
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueMapper, SkuSaleAttrValue> implements SkuSaleAttrValueService {

    @Override
    @RedisCache(prefix = "skuValueIdsMap:")
    public List<Map<String, Object>> getSkuValueIdsMap(Long spuId) {
        return this.baseMapper.getSkuValueIdsMap(spuId);
    }
}




