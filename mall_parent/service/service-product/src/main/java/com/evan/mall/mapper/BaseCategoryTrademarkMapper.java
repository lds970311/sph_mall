package com.evan.mall.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evan.mall.product.BaseCategoryTrademark;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 26966
 * @description 针对表【base_category_trademark(三级分类表)】的数据库操作Mapper
 * @createDate 2023-11-17 22:03:22
 * @Entity com.evan.mall.BaseCategoryTrademark
 */

@Mapper
public interface BaseCategoryTrademarkMapper extends BaseMapper<BaseCategoryTrademark> {

}




