package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.product.BaseSaleAttr;
import com.evan.mall.product.SpuInfo;
import com.evan.mall.service.BaseSaleAttrService;
import com.evan.mall.service.SpuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/product")
@Api(tags = "Spu管理")
public class SpuManagerController {
    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private BaseSaleAttrService baseSaleAttrService;


    @GetMapping("/{pageNo}/{limit}")
    @ApiOperation("分页查询spu")
    public Result<Map<String, Object>> getSpuInfoPage(@PathVariable Long limit,
                                                      @PathVariable Long pageNo,
                                                      Long category3Id) {
        Map<String, Object> result = this.spuInfoService.getSpuInfoPage(limit, pageNo, category3Id);
        return Result.ok(result);
    }


    @GetMapping("/baseSaleAttrList")
    @ApiOperation("获取基础销售属性")
    public Result<List<BaseSaleAttr>> baseSaleAttrList() {
        List<BaseSaleAttr> baseSaleAttrList = baseSaleAttrService.list();
        return Result.ok(baseSaleAttrList);
    }

    @PostMapping("/saveSpuInfo")
    @ApiOperation("保存spu属性")
    public Result<String> saveSpuInfo(@RequestBody SpuInfo spuInfo) {
        boolean saved = this.spuInfoService.saveSpuInfo(spuInfo);
        return saved ? Result.ok("保存spu属性成功") : Result.fail("保存spu失败！");
    }
}
