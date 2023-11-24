package com.evan.mall.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.SpuSaleAttr;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
 * @createDate 2023-11-17 22:03:22
 */
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    List<SpuSaleAttr> findSaleAttrList(Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);

}
