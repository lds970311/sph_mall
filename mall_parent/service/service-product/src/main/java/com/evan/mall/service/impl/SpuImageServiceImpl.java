package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.mapper.SpuImageMapper;
import com.evan.mall.product.SpuImage;
import com.evan.mall.service.SpuImageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 26966
 * @description 针对表【spu_image(商品图片表)】的数据库操作Service实现
 * @createDate 2023-11-17 22:03:22
 */
@Service
public class SpuImageServiceImpl extends ServiceImpl<SpuImageMapper, SpuImage>
        implements SpuImageService {

    @Override
    public List<SpuImage> getSpuImageListBySpuId(Long spuId) {
        LambdaQueryWrapper<SpuImage> spuImageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        spuImageLambdaQueryWrapper.eq(SpuImage::getSpuId, spuId);
        List<SpuImage> spuImageList = this.baseMapper.selectList(spuImageLambdaQueryWrapper);
        return spuImageList;
    }
}




