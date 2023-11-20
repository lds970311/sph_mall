package com.evan.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evan.mall.product.SkuAttrValue;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 26966
 * @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Mapper
 * @createDate 2023-11-18 00:04:27
 * @Entity com.evan.mall.product.SkuAttrValue
 */
@Mapper
public interface SkuAttrValueMapper extends BaseMapper<SkuAttrValue> {

}




