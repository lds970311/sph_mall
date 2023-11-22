package com.evan.mall.service;

import java.util.Map;

public interface SkuItemApiService {
    Map<String, Object> getSkuItemBySkuId(Long skuId);
}
