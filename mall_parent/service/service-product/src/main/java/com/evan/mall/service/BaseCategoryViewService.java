package com.evan.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.BaseCategoryView;

/**
 * @author 26966
 * @description 针对表【base_category_view2】的数据库操作Service
 * @createDate 2023-11-22 20:42:34
 */
public interface BaseCategoryViewService extends IService<BaseCategoryView> {

    BaseCategoryView findBaseCategoryByCategory3Id(Long category3Id);
}
