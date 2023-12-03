package com.evan.mall.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.BaseCategoryView;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【base_category_view2】的数据库操作Service
 * @createDate 2023-11-22 20:42:34
 */
public interface BaseCategoryViewService extends IService<BaseCategoryView> {

    BaseCategoryView findBaseCategoryByCategory3Id(Long category3Id);

    /**
     * 返回所有商品分类数据
     *
     * @return 商品所有分类数据
     */
    List<JSONObject> getBaseCategoryList();
}
