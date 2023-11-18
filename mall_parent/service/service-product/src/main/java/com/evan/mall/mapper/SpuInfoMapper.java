package com.evan.mall.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evan.mall.product.SpuInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 26966
 * @description 针对表【spu_info(商品表)】的数据库操作Mapper
 * @createDate 2023-11-17 22:03:22
 * @Entity com.evan.mall.SpuInfo
 */
@Mapper
public interface SpuInfoMapper extends BaseMapper<SpuInfo> {

}




