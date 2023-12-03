package com.evan.mall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.common.cache.RedisCache;
import com.evan.mall.mapper.BaseCategoryViewMapper;
import com.evan.mall.product.BaseCategoryView;
import com.evan.mall.service.BaseCategoryViewService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 26966
 * @description 针对表【base_category_view2】的数据库操作Service实现
 * @createDate 2023-11-22 20:42:34
 */
@Service
public class BaseCategoryViewServiceImpl extends ServiceImpl<BaseCategoryViewMapper, BaseCategoryView>
        implements BaseCategoryViewService {

    @Override
    @RedisCache(prefix = "categoryView:")
    public BaseCategoryView findBaseCategoryByCategory3Id(Long category3Id) {

        return this.baseMapper.selectById(category3Id);
    }

    /**
     * 返回所有商品分类数据
     *
     * @return 商品所有分类数据
     */
    @Override
    @RedisCache(prefix = "baseCategoryList:")
    public List<JSONObject> getBaseCategoryList() {
        List<JSONObject> result = new ArrayList<>();
        // 获取所有分类数据
        List<BaseCategoryView> baseCategoryViews = this.baseMapper.selectList(null);
        Map<Long, List<BaseCategoryView>> category1ListMap = baseCategoryViews.stream()
                .collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
        Set<Map.Entry<Long, List<BaseCategoryView>>> entries = category1ListMap.entrySet();
        int index = 0;
        for (Map.Entry<Long, List<BaseCategoryView>> entry : entries) {
            //1 : category1的所有数据
            JSONObject category1 = new JSONObject();
            Long category1Id = entry.getKey();
            List<BaseCategoryView> category2List = entry.getValue();
            category1.put("index", ++index);
            category1.put("categoryId", category1Id);
            category1.put("categoryName", category2List.get(0).getCategory1Name());
            List<JSONObject> category2Child = new ArrayList<>();
            category1.put("categoryChild", category2Child);

            Map<Long, List<BaseCategoryView>> category2ListMap = category2List.stream()
                    .collect(Collectors.
                            groupingBy(BaseCategoryView::getCategory2Id));
            Set<Map.Entry<Long, List<BaseCategoryView>>> entries1 = category2ListMap.entrySet();

            for (Map.Entry<Long, List<BaseCategoryView>> entry1 : entries1) {
                //2 : category2的所有数据
                JSONObject category2 = new JSONObject();
                Long category2Id = entry1.getKey();
                List<BaseCategoryView> category3List = entry1.getValue();
                category2.put("categoryId", category2Id);
                category2.put("categoryName", category3List.get(0).getCategory2Name());
                List<JSONObject> category3Child = new ArrayList<>();
                category2.put("categoryChild", category3Child);

                for (BaseCategoryView baseCategoryView : category3List) {
                    JSONObject category3 = new JSONObject();
                    category3.put("categoryId", baseCategoryView.getCategory3Id());
                    category3.put("categoryName", baseCategoryView.getCategory3Name());
                    category3Child.add(category3);
                }
                category2.put("categoryChild", category3Child);
                category2Child.add(category2);
            }
            category1.put("categoryChild", category2Child);
            result.add(category1);
        }
        return result;
    }

}




