package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.product.BaseTrademark;
import com.evan.mall.service.BaseTrademarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/product/baseTrademark")
@Api(tags = "品牌管理")
public class BaseTrademarkController {
    @Autowired
    private BaseTrademarkService baseTrademarkService;


    @GetMapping("{pageNo}/{limit}")
    @ApiOperation("分页查询品牌数据")
    public Result<Map<String, Object>> getTradeMarkPage(@PathVariable Long pageNo,
                                                        @PathVariable Long limit) {
        Map<String, Object> result = this.baseTrademarkService.getTradeMarkPage(limit, pageNo);
        return Result.ok(result);
    }

    @PostMapping("/save")
    @ApiOperation("保存品牌信息")
    public Result<String> saveTrademark(@RequestBody BaseTrademark baseTrademark) {
        boolean save = this.baseTrademarkService.save(baseTrademark);
        //TODO 文件上传至minIO
        return save ? Result.ok("保存品牌信息成功") : Result.fail("保存品牌信息失败！");
    }

    @PutMapping("/update")
    @ApiOperation("更新品牌信息")
    public Result<String> updateTradeMarkInfo(@RequestBody BaseTrademark baseTrademark) {
        boolean b = this.baseTrademarkService.updateById(baseTrademark);
        return b ? Result.ok("更新品牌信息成功") : Result.fail("更新品牌信息失败！");
    }

    @DeleteMapping("/remove/{id}")
    @ApiOperation("删除品牌信息")
    public Result<String> deleteTrademarkById(@PathVariable Long id) {
        boolean b = this.baseTrademarkService.removeById(id);
        return b ? Result.ok("删除品牌信息成功") : Result.fail("删除品牌信息失败！");
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据id查询品牌信息")
    public Result<BaseTrademark> getTrademarkById(@PathVariable Long id) {
        BaseTrademark byId = this.baseTrademarkService.getById(id);
        return Result.ok(byId);
    }

    @DeleteMapping("/batchDelete")
    @ApiOperation("批量删除品牌")
    public Result<String> batchDeleteTrademark(@RequestBody List<Long> ids) {
        boolean b = this.baseTrademarkService.removeBatchByIds(ids);
        return b ? Result.ok("批量删除品牌信息成功") : Result.fail("批量删除品牌信息失败！");
    }
}
