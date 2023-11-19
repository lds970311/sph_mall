package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.product.BaseTrademark;
import com.evan.mall.product.CategoryTrademarkVo;
import com.evan.mall.service.BaseCategoryTrademarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/baseCategoryTrademark")
@Api("分类品牌管理")
public class BaseCategoryTrademarkController {
    @Autowired
    private BaseCategoryTrademarkService baseCategoryTrademarkService;


    @GetMapping("/findTrademarkList/{category3Id}")
    @ApiOperation("根据category3Id获取品牌列表")
    public Result<List<BaseTrademark>> getTradeMarkListByCategory3Id(@PathVariable Long category3Id) {
        List<BaseTrademark> baseTrademarkList = baseCategoryTrademarkService.getTradeMarkListById(category3Id);
        return Result.ok(baseTrademarkList);
    }

    @DeleteMapping("/remove/{category3Id}/{trademarkId}")
    @ApiOperation("删除品牌分类")
    public Result<String> remove(@PathVariable Long category3Id, @PathVariable Long trademarkId) {
        boolean isDelete = this.baseCategoryTrademarkService.removeTradeMark(category3Id, trademarkId);
        return isDelete ? Result.ok("删除成功") : Result.fail("删除失败");
    }

    @GetMapping("/findCurrentTrademarkList/{category3Id}")
    @ApiOperation("根据category3Id查找未关联分类的品牌")
    public Result<List<BaseTrademark>> findCurrentTrademarkList(@PathVariable Long category3Id) {
        List<BaseTrademark> baseTrademarkList = this.baseCategoryTrademarkService.findCurrentTrademarkList(category3Id);
        return Result.ok(baseTrademarkList);
    }

    @PostMapping("/save")
    @ApiOperation("保存品牌信息")
    public Result<String> saveTrademark(@RequestBody CategoryTrademarkVo categoryTrademarkVo) {
        boolean saved = this.baseCategoryTrademarkService.saveTrademark(categoryTrademarkVo);
        return saved ? Result.ok("保存成功！") : Result.fail("保存失败");
    }
}
