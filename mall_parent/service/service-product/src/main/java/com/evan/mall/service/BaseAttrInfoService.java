package com.evan.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.mall.product.BaseAttrInfo;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【base_attr_info(属性表)】的数据库操作Service
 * @createDate 2023-11-23 22:07:20
 */
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    List<BaseAttrInfo> getAttrListBySkuId(Long skuId);

}
