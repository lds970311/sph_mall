package com.evan.mall.service;


import com.evan.mall.product.BaseAttrInfo;
import com.evan.mall.product.BaseCategory1;
import com.evan.mall.product.BaseCategory2;
import com.evan.mall.product.BaseCategory3;

import java.util.List;

public interface ManagerService {
    /**
     * 查找一级分类
     *
     * @return
     */
    List<BaseCategory1> getCategory1();

    List<BaseCategory2> getCategory2(Long category1Id);

    List<BaseCategory3> getCategory3(Long category2Id);

    /**
     * 根据分类id查询平台属性
     *
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> getAttrInfo(String category1Id, String category2Id, String category3Id);
}
