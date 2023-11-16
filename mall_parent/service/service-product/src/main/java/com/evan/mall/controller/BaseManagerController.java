package com.evan.mall.controller;

import com.evan.mall.common.result.Result;
import com.evan.mall.product.BaseAttrInfo;
import com.evan.mall.product.BaseCategory1;
import com.evan.mall.product.BaseCategory2;
import com.evan.mall.product.BaseCategory3;
import com.evan.mall.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class BaseManagerController {
    @Autowired
    private ManagerService managerService;

    @GetMapping("/getCategory1")
    public Result<List<BaseCategory1>> getCategory1() {
        List<BaseCategory1> baseCategory1List = managerService.getCategory1();

        return Result.ok(baseCategory1List);
    }

    @GetMapping("/getCategory2/{category1Id}")
    public Result<List<BaseCategory2>> getCategory2(@PathVariable Long category1Id) {
        List<BaseCategory2> baseCategory2List = managerService.getCategory2(category1Id);
        return Result.ok(baseCategory2List);
    }

    @GetMapping("/getCategory3/{category2Id}")
    public Result<List<BaseCategory3>> getCategory3(@PathVariable Long category2Id) {
        List<BaseCategory3> baseCategory3List = managerService.getCategory3(category2Id);
        return Result.ok(baseCategory3List);
    }

    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    private Result<List<BaseAttrInfo>> attrInfoList(@PathVariable String category1Id,
                                                    @PathVariable String category2Id,
                                                    @PathVariable String category3Id) {
        List<BaseAttrInfo> baseAttrInfoList = managerService.getAttrInfo(category1Id, category2Id, category3Id);
        return Result.ok(baseAttrInfoList);
    }
}
