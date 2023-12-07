package com.evan.mall.client.impl;

import com.evan.mall.client.ListFeignClient;
import com.evan.mall.common.result.Result;
import org.springframework.stereotype.Component;

@Component
public class ListDegradeFeignClient implements ListFeignClient {
    @Override
    public Result<Void> incrHotScore(Long skuId) {
        return Result.fail();
    }
}
