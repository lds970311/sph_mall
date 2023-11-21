package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.evan.mall.mapper.*;
import com.evan.mall.product.*;
import com.evan.mall.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseCategory1> getCategory1() {
        List<BaseCategory1> baseCategory1List = baseCategory1Mapper.selectList(null);
        return baseCategory1List;
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        LambdaQueryWrapper<BaseCategory2> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseCategory2::getCategory1Id, category1Id);
        List<BaseCategory2> category2List = baseCategory2Mapper.selectList(queryWrapper);
        return category2List;
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        LambdaQueryWrapper<BaseCategory3> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseCategory3::getCategory2Id, category2Id);
        List<BaseCategory3> category3List = this.baseCategory3Mapper.selectList(wrapper);
        return category3List;
    }

    /**
     * 根据分类id查询平台属性
     *
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    @Override
    public List<BaseAttrInfo> getAttrInfo(String category1Id, String category2Id, String category3Id) {
        return baseAttrInfoMapper.selectAttrInfoList(category1Id, category2Id, category3Id);
    }

    /**
     * 保存平台属性(新增/添加)
     *
     * @param baseAttrInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBaseAttrInfo(BaseAttrInfo baseAttrInfo) {
        Long id = baseAttrInfo.getId();
        if (id == null) {
            //新增
            int insert = baseAttrInfoMapper.insert(baseAttrInfo);
            if (insert == 0) return false;
        } else {
            //修改
            int updated = baseAttrInfoMapper.updateById(baseAttrInfo);
            if (updated == 0) return false;
            //删除原来的平台属性,然后新增
            LambdaQueryWrapper<BaseAttrValue> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BaseAttrValue::getAttrId, baseAttrInfo.getId());
            this.baseAttrValueMapper.delete(wrapper);
        }

        //新增平台属性值
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if (!CollectionUtils.isEmpty(attrValueList)) {
            attrValueList.forEach(attr -> {
                BaseAttrValue baseAttrValue = new BaseAttrValue();
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValue.setValueName(attr.getValueName());
                this.baseAttrValueMapper.insert(baseAttrValue);
            });
        }
        return true;
    }

    /**
     * 根据属性id查找属性列表
     *
     * @param attrId
     * @return
     */
    @Override
    public BaseAttrInfo getAttrInfoById(Long attrId) {
        BaseAttrInfo baseAttrInfo = this.baseAttrInfoMapper.selectById(attrId);
        //根据attrId获取属性值集合
        LambdaQueryWrapper<BaseAttrValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BaseAttrValue::getAttrId, attrId);
        List<BaseAttrValue> baseAttrValueList = this.baseAttrValueMapper.selectList(wrapper);
        baseAttrInfo.setAttrValueList(baseAttrValueList);
        return baseAttrInfo;
    }
}
