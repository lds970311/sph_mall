package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.mapper.SpuPosterMapper;
import com.evan.mall.product.SpuPoster;
import com.evan.mall.service.SpuPosterService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【spu_poster(商品海报表)】的数据库操作Service实现
 * @createDate 2023-11-17 22:03:22
 */
@Service
public class SpuPosterServiceImpl extends ServiceImpl<SpuPosterMapper, SpuPoster> implements SpuPosterService {

    @Override
    public List<SpuPoster> findPosterBySpuId(Long spuId) {
        LambdaQueryWrapper<SpuPoster> spuPosterLambdaQueryWrapper = new LambdaQueryWrapper<>();
        spuPosterLambdaQueryWrapper.eq(SpuPoster::getSpuId, spuId);
        return this.baseMapper.selectList(spuPosterLambdaQueryWrapper);
    }
}




