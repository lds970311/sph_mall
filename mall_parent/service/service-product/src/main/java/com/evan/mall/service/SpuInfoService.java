package com.evan.mall.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.SpuInfo;

import java.util.List;
import java.util.Map;

/**
 * @author 26966
 * @description 针对表【spu_info(商品表)】的数据库操作Service
 * @createDate 2023-11-17 22:03:22
 */
public interface SpuInfoService extends IService<SpuInfo> {

    Map<String, Object> getSpuInfoPage(Long limit, Long pageNo, Long category3Id);

    boolean saveSpuInfo(SpuInfo spuInfo);

    List<SpuInfo> getAllSpuInfoByCategory3Id(Long category3Id);
}
