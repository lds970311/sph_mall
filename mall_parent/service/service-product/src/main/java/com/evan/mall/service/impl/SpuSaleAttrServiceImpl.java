package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.common.cache.RedisCache;
import com.evan.mall.mapper.SpuSaleAttrMapper;
import com.evan.mall.product.SpuSaleAttr;
import com.evan.mall.service.SpuSaleAttrService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service实现
 * @createDate 2023-11-17 22:03:22
 */
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr> implements SpuSaleAttrService {

    @Override
    public List<SpuSaleAttr> findSaleAttrList(Long spuId) {
        return this.baseMapper.findSaleAttrList(spuId);
    }

    @Override
    @RedisCache(prefix = "spuSaleAttrListCheckBySku:")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return this.baseMapper.getSpuSaleAttrListCheckBySku(skuId, spuId);
    }
}






