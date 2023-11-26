package com.evan.mall.item.client;

import com.evan.mall.common.result.Result;
import com.evan.mall.item.client.impl.ItemDegradeFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "service-item", fallback = ItemDegradeFeignClient.class)
public interface ItemFeignClient {
    @GetMapping("/api/item/{skuId}")
    @ApiOperation("根据skuId查询sku详情")
    Result<Map<String, Object>> getSkuItem(@PathVariable Long skuId);
}
