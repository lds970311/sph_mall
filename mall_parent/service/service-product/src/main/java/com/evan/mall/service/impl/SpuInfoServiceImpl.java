package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.mapper.SpuInfoMapper;
import com.evan.mall.product.SpuInfo;
import com.evan.mall.service.SpuInfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 26966
 * @description 针对表【spu_info(商品表)】的数据库操作Service实现
 * @createDate 2023-11-17 22:03:22
 */
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
        implements SpuInfoService {

    @Override
    public Map<String, Object> getSpuInfoPage(Long limit, Long pageNo, Long category3Id) {
        Map<String, Object> result = new HashMap<>();
        Page<SpuInfo> infoPage = new Page<>(pageNo, limit);
        LambdaQueryWrapper<SpuInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpuInfo::getCategory3Id, category3Id);
        this.baseMapper.selectPage(infoPage, wrapper);
        List<SpuInfo> records = infoPage.getRecords();
        long current = infoPage.getCurrent();
        long total = infoPage.getTotal();
        result.put("records", records);
        result.put("current", current);
        result.put("total", total);
        return result;
    }
}




