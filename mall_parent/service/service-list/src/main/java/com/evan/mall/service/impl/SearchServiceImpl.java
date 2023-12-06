package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.evan.mall.dao.GoodsDao;
import com.evan.mall.list.Goods;
import com.evan.mall.list.SearchAttr;
import com.evan.mall.product.BaseAttrInfo;
import com.evan.mall.product.BaseCategoryView;
import com.evan.mall.product.BaseTrademark;
import com.evan.mall.product.SkuInfo;
import com.evan.mall.product.client.ProductFeignClient;
import com.evan.mall.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private GoodsDao goodsDao;

    @Qualifier("com.evan.mall.product.client.ProductFeignClient")
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public boolean upperGoods(Long skuId) {
        if (skuId == null) {
            return false;
        }
        //封装Goods数据
        Goods goods = new Goods();
        //设置skuInfo
        SkuInfo skuInfo = this.productFeignClient.getSkuInfo(skuId);
        if (null == skuInfo) {
            return false;
        }
        Long tmId = skuInfo.getTmId();
        BigDecimal skuPrice = this.productFeignClient.getSkuPrice(skuId);
        if (null != skuPrice) {
            goods.setPrice(skuPrice.doubleValue());
        }
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setCreateTime(new Date());
        goods.setTmId(tmId);
        //设置分类信息
        BaseCategoryView baseCategory = this.productFeignClient.findBaseCategory(skuInfo.getCategory3Id());
        if (null != baseCategory) {
            goods.setCategory1Id(baseCategory.getCategory1Id());
            goods.setCategory2Id(baseCategory.getCategory2Id());
            goods.setCategory3Id(baseCategory.getCategory3Id());
            goods.setCategory1Name(baseCategory.getCategory1Name());
            goods.setCategory2Name(baseCategory.getCategory2Name());
            goods.setCategory3Name(baseCategory.getCategory3Name());
        }

        //设置品牌
        BaseTrademark trademark = this.productFeignClient.getTrademark(tmId);
        if (null != trademark) {
            goods.setTmLogoUrl(trademark.getLogoUrl());
            goods.setTmName(trademark.getTmName());
        }

        //设置平台属性
        List<BaseAttrInfo> attrList = this.productFeignClient.getAttrList(skuId);
        if (CollectionUtils.isNotEmpty(attrList)) {
            List<SearchAttr> searchAttrList = new ArrayList<>();
            for (BaseAttrInfo baseAttrInfo : attrList) {
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(baseAttrInfo.getId());
                searchAttr.setAttrName(baseAttrInfo.getAttrName());
                searchAttr.setAttrValue(baseAttrInfo.getAttrValueList().get(0).getValueName());
                searchAttrList.add(searchAttr);
            }
            goods.setAttrs(searchAttrList);
        }
        this.goodsDao.save(goods);
        return true;
    }
}
