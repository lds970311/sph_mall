package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.item.client.ItemFeignClient;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

;

@Api(tags = "商品详情")
@Controller
public class ItemController {
    @Qualifier("com.evan.mall.item.client.ItemFeignClient")
    @Autowired
    private ItemFeignClient itemFeignClient;

    @GetMapping("/{skuId}.html")
    public String getItem(@PathVariable Long skuId, Model model) {
        Result<Map<String, Object>> skuItem = this.itemFeignClient.getSkuItem(skuId);
        model.addAllAttributes(skuItem.getData());
        return "item/item";
    }
}
