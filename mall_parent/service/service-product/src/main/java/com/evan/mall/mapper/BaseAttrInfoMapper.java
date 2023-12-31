package com.evan.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evan.mall.product.BaseAttrInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【base_attr_info(属性表)】的数据库操作Mapper
 * @createDate 2023-11-23 22:07:20
 * @Entity com.evan.mall.product.BaseAttrInfo
 */
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {

    List<BaseAttrInfo> getAttrListBySkuId(Long skuId);

    List<BaseAttrInfo> selectAttrInfoList(@Param("category1Id") String category1Id,
                                          @Param("category2Id") String category2Id,
                                          @Param("category3Id") String category3Id);
}




