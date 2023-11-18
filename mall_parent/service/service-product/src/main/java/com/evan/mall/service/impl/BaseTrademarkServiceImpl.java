package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.mapper.BaseTrademarkMapper;
import com.evan.mall.product.BaseTrademark;
import com.evan.mall.service.BaseTrademarkService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 26966
 * @description 针对表【base_trademark(品牌表)】的数据库操作Service实现
 * @createDate 2023-11-17 22:03:22
 */
@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper, BaseTrademark>
        implements BaseTrademarkService {

    @Override
    public Map<String, Object> getTradeMarkPage(Long limit, Long pageNo) {
        Page<BaseTrademark> baseTrademarkPage = new Page<>(pageNo, limit);
        this.baseMapper.selectPage(baseTrademarkPage, null);
        List<BaseTrademark> records = baseTrademarkPage.getRecords();
        Long total = baseTrademarkPage.getTotal();
        Long current = baseTrademarkPage.getCurrent();
        Map<String, Object> map = new HashMap<>();
        map.put("records", records);
        map.put("current", current);
        map.put("total", total);
        return map;
    }
}




