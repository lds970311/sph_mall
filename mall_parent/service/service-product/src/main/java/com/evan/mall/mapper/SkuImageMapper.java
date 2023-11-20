package com.evan.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.evan.mall.product.SkuImage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 26966
 * @description 针对表【sku_image(库存单元图片表)】的数据库操作Mapper
 * @createDate 2023-11-18 00:04:27
 * @Entity com.evan.mall.product.SkuImage
 */
@Mapper
public interface SkuImageMapper extends BaseMapper<SkuImage> {

}




