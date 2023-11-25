package com.evan.mall.item.client.impl;

import com.evan.mall.common.result.Result;
import com.evan.mall.item.client.ItemFeignClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ItemDegradeFeignClient implements ItemFeignClient {
    @Override
    public Result<Map<String, Object>> getSkuItem(Long skuId) {
        return Result.fail();
    }
}
