package com.evan.mall.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.SpuImage;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【spu_image(商品图片表)】的数据库操作Service
 * @createDate 2023-11-17 22:03:22
 */
public interface SpuImageService extends IService<SpuImage> {

    List<SpuImage> getSpuImageListBySpuId(Long spuId);

}
