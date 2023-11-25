package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.mapper.BaseAttrInfoMapper;
import com.evan.mall.product.BaseAttrInfo;
import com.evan.mall.service.BaseAttrInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
 * @createDate 2023-11-23 22:07:20
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo> implements BaseAttrInfoService {

    @Override
    public List<BaseAttrInfo> getAttrListBySkuId(Long skuId) {
        return this.baseMapper.getAttrListBySkuId(skuId);
    }

}




