package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.mapper.BaseCategoryTrademarkMapper;
import com.evan.mall.mapper.BaseTrademarkMapper;
import com.evan.mall.product.BaseCategoryTrademark;
import com.evan.mall.product.BaseTrademark;
import com.evan.mall.product.CategoryTrademarkVo;
import com.evan.mall.service.BaseCategoryTrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 26966
 * @description 针对表【base_category_trademark(三级分类表)】的数据库操作Service实现
 * @createDate 2023-11-17 22:03:22
 */
@Service
@SuppressWarnings("all")
public class BaseCategoryTrademarkServiceImpl extends ServiceImpl<BaseCategoryTrademarkMapper, BaseCategoryTrademark>
        implements BaseCategoryTrademarkService {
    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;

    @Override
    public List<BaseTrademark> getTradeMarkListById(Long category3Id) {
        //根据分类id查询品牌的id
        LambdaQueryWrapper<BaseCategoryTrademark> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = this.baseMapper.selectList(wrapper);
        List<Long> trademarkIds = null;
        if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)) {
            trademarkIds = baseCategoryTrademarkList.stream()
                    .map(BaseCategoryTrademark::getTrademarkId)
                    .collect(Collectors.toList());
        }

        return this.baseTrademarkMapper.selectBatchIds(trademarkIds);
    }

    @Override
    public boolean removeTradeMark(Long category3Id, Long trademarkId) {
        LambdaQueryWrapper<BaseCategoryTrademark> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id)
                .eq(BaseCategoryTrademark::getTrademarkId, trademarkId);

        int delete = this.baseMapper.delete(wrapper);
        return delete != 0 ? true : false;
    }

    @Override
    public List<BaseTrademark> findCurrentTrademarkList(Long category3Id) {
        //获取已关联品牌的id
        LambdaQueryWrapper<BaseCategoryTrademark> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseCategoryTrademark::getCategory3Id, category3Id)
                .select(BaseCategoryTrademark::getTrademarkId);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = this.baseMapper.selectList(wrapper);
        List<Long> trademarkIds = null;
        List<BaseTrademark> baseTrademarkList = null;
        if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)) {
            trademarkIds = baseCategoryTrademarkList.stream()
                    .map(BaseCategoryTrademark::getTrademarkId)
                    .collect(Collectors.toList());

            //查询未关联的品牌
            LambdaQueryWrapper<BaseTrademark> baseTrademarkLambdaQueryWrapper = new LambdaQueryWrapper<>();
            baseTrademarkLambdaQueryWrapper.notIn(BaseTrademark::getId, trademarkIds);
            baseTrademarkList = this.baseTrademarkMapper.selectList(baseTrademarkLambdaQueryWrapper);
        }

        return baseTrademarkList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTrademark(CategoryTrademarkVo categoryTrademarkVo) {
        //获取trademark的id
        List<Long> trademarkIdList = categoryTrademarkVo.getTrademarkIdList();
        for (Long trademarkId : trademarkIdList) {
            BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();
            baseCategoryTrademark.setTrademarkId(trademarkId);
            baseCategoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
            this.baseMapper.insert(baseCategoryTrademark);
        }
        return true;
    }
}




