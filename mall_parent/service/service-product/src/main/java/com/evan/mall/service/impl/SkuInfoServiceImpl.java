package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.mapper.SkuAttrValueMapper;
import com.evan.mall.mapper.SkuImageMapper;
import com.evan.mall.mapper.SkuInfoMapper;
import com.evan.mall.mapper.SkuSaleAttrValueMapper;
import com.evan.mall.product.SkuAttrValue;
import com.evan.mall.product.SkuImage;
import com.evan.mall.product.SkuInfo;
import com.evan.mall.product.SkuSaleAttrValue;
import com.evan.mall.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 26966
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2023-11-18 00:04:27
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Override
    public boolean saveSkuInfo(SkuInfo skuInfo) {
        skuInfo.setIsSale(0);
        this.baseMapper.insert(skuInfo);
        //获取sku属性列表
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                this.skuAttrValueMapper.insert(skuAttrValue);
            }
        }
        //sku图片
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (!CollectionUtils.isEmpty(skuImageList)) {
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfo.getId());
                this.skuImageMapper.insert(skuImage);
            }
        }
        //获取sku销售属性
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (!CollectionUtils.isEmpty(skuSaleAttrValueList)) {
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                this.skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> getSkuInfoByPage(Long pageNo, Long limit) {
        Page<SkuInfo> skuInfoPage = new Page<>(pageNo, limit);
        this.baseMapper.selectPage(skuInfoPage, null);
        Map<String, Object> map = new HashMap<>();
        long current = skuInfoPage.getCurrent();
        List<SkuInfo> records = skuInfoPage.getRecords();
        long total = skuInfoPage.getTotal();
        map.put("total", total);
        map.put("rows", records);
        map.put("current", current);
        return map;
    }

    @Override
    public boolean onSale(Long skuId) {
        //获取sku
        SkuInfo skuInfo = this.baseMapper.selectById(skuId);
        if (skuInfo.getIsSale() == 0) {
            //未上架，上架
            skuInfo.setIsSale(1);
        } else {
            skuInfo.setIsSale(0);
        }
        this.baseMapper.updateById(skuInfo);
        return true;
    }
}




