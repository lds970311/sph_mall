package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.list.Goods;
import com.evan.mall.list.SearchParam;
import com.evan.mall.list.SearchResponseVo;
import com.evan.mall.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/list")
@SuppressWarnings("all")
@Api(tags = "商品搜索")
public class ListApiController {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private SearchService searchService;

    @ApiOperation("创建索引库")
    @GetMapping("/inner/createIndex")
    public Result createIndex() {
        //创建goods索引库
        this.elasticsearchRestTemplate.createIndex(Goods.class);
        this.elasticsearchRestTemplate.putMapping(Goods.class);
        return Result.ok("创建索引库成功！");
    }


    @GetMapping("/inner/upperGoods/{skuId}")
    @ApiOperation("商品上架")
    public Result upperGoods(@PathVariable Long skuId) {
        boolean success = this.searchService.upperGoods(skuId);
        return success ? Result.ok().message("商品上架成功！") : Result.fail().message("商品上架失败！");
    }


    @GetMapping("/inner/lowerGoods/{skuId}")
    @ApiOperation("商品下架")
    public Result lowerGoods(@PathVariable Long skuId) {
        boolean success = this.searchService.lowerGoods(skuId);
        return success ? Result.ok().message("商品下架成功！") : Result.fail().message("商品下架失败！");
    }

    @GetMapping("/inner/incrHotScore/{skuId}")
    @ApiOperation("更新商品热度")
    public Result incrHotScore(@PathVariable Long skuId) {
        boolean success = this.searchService.incrHotScore(skuId);
        return success ? Result.ok().message("更新商品热度成功！") : Result.fail().message("更新商品热度失败！");
    }

    @PostMapping
    @ApiOperation("商品搜索")
    public Result<SearchResponseVo> list(@RequestBody SearchParam searchParam) {
        SearchResponseVo searchResponseVo = this.searchService.searchGoods(searchParam);
        return Result.ok(searchResponseVo);
    }
}
