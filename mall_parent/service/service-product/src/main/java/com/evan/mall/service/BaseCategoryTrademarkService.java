package com.evan.mall.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.BaseCategoryTrademark;
import com.evan.mall.product.BaseTrademark;
import com.evan.mall.product.CategoryTrademarkVo;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【base_category_trademark(三级分类表)】的数据库操作Service
 * @createDate 2023-11-17 22:03:22
 */
public interface BaseCategoryTrademarkService extends IService<BaseCategoryTrademark> {

    List<BaseTrademark> getTradeMarkListById(Long category3Id);

    boolean removeTradeMark(Long category3Id, Long trademarkId);

    List<BaseTrademark> findCurrentTrademarkList(Long category3Id);

    boolean saveTrademark(CategoryTrademarkVo categoryTrademarkVo);
}
