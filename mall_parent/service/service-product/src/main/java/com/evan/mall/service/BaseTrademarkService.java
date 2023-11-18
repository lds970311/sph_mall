package com.evan.mall.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.BaseTrademark;

import java.util.Map;

/**
 * @author 26966
 * @description 针对表【base_trademark(品牌表)】的数据库操作Service
 * @createDate 2023-11-17 22:03:22
 */
public interface BaseTrademarkService extends IService<BaseTrademark> {

    Map<String, Object> getTradeMarkPage(Long limit, Long pageNo);
}
