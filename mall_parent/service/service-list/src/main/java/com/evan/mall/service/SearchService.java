package com.evan.mall.service;

import com.evan.mall.list.SearchParam;
import com.evan.mall.list.SearchResponseVo;

public interface SearchService {
    boolean upperGoods(Long skuId);

    boolean lowerGoods(Long skuId);

    /**
     * 更新商品热度
     *
     * @param skuId
     * @return
     */
    boolean incrHotScore(Long skuId);

    /**
     * 根据条件搜索商品
     *
     * @param searchParam ：用户传递的检索条件
     * @return ：返回检索结果
     */
    SearchResponseVo searchGoods(SearchParam searchParam);
}
