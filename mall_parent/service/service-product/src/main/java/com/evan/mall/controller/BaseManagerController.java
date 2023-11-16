package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.product.*;
import com.evan.mall.service.ManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@Api(tags = "平台属性管理")
public class BaseManagerController {
    @Autowired
    private ManagerService managerService;

    @GetMapping("/getCategory1")
    @ApiOperation("获取商品一级分类")
    public Result<List<BaseCategory1>> getCategory1() {
        List<BaseCategory1> baseCategory1List = managerService.getCategory1();

        return Result.ok(baseCategory1List);
    }

    @GetMapping("/getCategory2/{category1Id}")
    @ApiOperation("获取商品二级分类")
    public Result<List<BaseCategory2>> getCategory2(@PathVariable Long category1Id) {
        List<BaseCategory2> baseCategory2List = managerService.getCategory2(category1Id);
        return Result.ok(baseCategory2List);
    }

    @GetMapping("/getCategory3/{category2Id}")
    @ApiOperation("获取商品三级分类")
    public Result<List<BaseCategory3>> getCategory3(@PathVariable Long category2Id) {
        List<BaseCategory3> baseCategory3List = managerService.getCategory3(category2Id);
        return Result.ok(baseCategory3List);
    }

    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    @ApiOperation("根据分类id查询平台属性")
    private Result<List<BaseAttrInfo>> attrInfoList(@PathVariable String category1Id,
                                                    @PathVariable String category2Id,
                                                    @PathVariable String category3Id) {
        List<BaseAttrInfo> baseAttrInfoList = managerService.getAttrInfo(category1Id, category2Id, category3Id);
        return Result.ok(baseAttrInfoList);
    }

    @PostMapping("/saveAttrInfo")
    @ApiOperation("保存平台属性")
    public Result<String> saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo) {
        return managerService.saveBaseAttrInfo(baseAttrInfo) ? Result.ok("保存平台属性成功") : Result.fail("保存平台属性失败!");
    }

    @GetMapping("/getAttrValueList/{attrId}")
    @ApiOperation("根据属性id获取属性值列表")
    public Result<List<BaseAttrValue>> getAttrInfoById(@PathVariable Long attrId) {
        BaseAttrInfo baseAttrInfo = managerService.getAttrInfoById(attrId);
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        return Result.ok(attrValueList);
    }
}
