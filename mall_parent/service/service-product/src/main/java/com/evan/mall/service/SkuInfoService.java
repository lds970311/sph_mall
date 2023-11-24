package com.evan.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.SkuInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author 26966
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service
 * @createDate 2023-11-18 00:04:27
 */
public interface SkuInfoService extends IService<SkuInfo> {

    boolean saveSkuInfo(SkuInfo skuInfo);

    Map<String, Object> getSkuInfoByPage(Long pageNo, Long limit);

    boolean onSale(Long skuId);

    List<SkuInfo> findSkuListBySpuId(Long spuId);

    SkuInfo getSkuInfo(Long skuId);

    /**
     * 查询实时价格
     *
     * @param skuId
     * @return
     */
    BigDecimal getSkuPrice(Long skuId);
}
