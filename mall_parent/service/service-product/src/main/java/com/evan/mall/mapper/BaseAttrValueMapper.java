package com.evan.mall.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evan.mall.product.BaseAttrValue;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 26966
 * @description 针对表【base_attr_value(属性值表)】的数据库操作Mapper
 * @createDate 2023-11-15 23:42:35
 * @Entity com.evan.mall.domain.BaseAttrValue
 */
@Mapper
public interface BaseAttrValueMapper extends BaseMapper<BaseAttrValue> {

}




