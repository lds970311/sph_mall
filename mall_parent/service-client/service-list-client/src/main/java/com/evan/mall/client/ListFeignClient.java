package com.evan.mall.client;

import com.evan.mall.client.impl.ListDegradeFeignClient;
import com.evan.mall.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-list", fallback = ListDegradeFeignClient.class)
public interface ListFeignClient {
    @GetMapping("/api/list/inner/incrHotScore/{skuId}")
    @ApiOperation("更新商品热度")
    Result<Void> incrHotScore(@PathVariable Long skuId);
}
