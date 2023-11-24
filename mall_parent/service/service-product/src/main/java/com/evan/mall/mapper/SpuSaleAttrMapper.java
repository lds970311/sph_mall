package com.evan.mall.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evan.mall.product.SpuSaleAttr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Mapper
 * @createDate 2023-11-17 22:03:22
 * @Entity com.evan.mall.SpuSaleAttr
 */
@Mapper
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {

    List<SpuSaleAttr> findSaleAttrList(Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);
}




