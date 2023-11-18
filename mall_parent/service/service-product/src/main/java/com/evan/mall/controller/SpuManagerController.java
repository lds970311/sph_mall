package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.service.SpuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/product")
@Api(tags = "Spu管理")
public class SpuManagerController {
    @Autowired
    private SpuInfoService spuInfoService;


    @GetMapping("/{pageNo}/{limit}")
    @ApiOperation("分页查询spu")
    public Result<Map<String, Object>> getSpuInfoPage(@PathVariable Long limit,
                                                      @PathVariable Long pageNo,
                                                      Long category3Id) {
        Map<String, Object> result = this.spuInfoService.getSpuInfoPage(limit, pageNo, category3Id);
        return Result.ok(result);
    }


}
