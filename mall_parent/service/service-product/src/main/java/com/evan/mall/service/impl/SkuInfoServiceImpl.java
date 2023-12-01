package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.mall.common.constant.RedisConst;
import com.evan.mall.mapper.SkuAttrValueMapper;
import com.evan.mall.mapper.SkuImageMapper;
import com.evan.mall.mapper.SkuInfoMapper;
import com.evan.mall.mapper.SkuSaleAttrValueMapper;
import com.evan.mall.product.SkuAttrValue;
import com.evan.mall.product.SkuImage;
import com.evan.mall.product.SkuInfo;
import com.evan.mall.product.SkuSaleAttrValue;
import com.evan.mall.service.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 26966
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2023-11-18 00:04:27
 */
@Service
@SuppressWarnings("all")
@Slf4j
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

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
        //告诉布隆过滤器，skuId已经被使用
        RBloomFilter<Object> bloomFilter = this.redissonClient.getBloomFilter(RedisConst.SKUKEY_PREFIX);
        bloomFilter.add(skuInfo.getId());
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

    @Override
    public List<SkuInfo> findSkuListBySpuId(Long spuId) {
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuInfo::getSpuId, spuId);
        List<SkuInfo> skuInfoList = this.baseMapper.selectList(wrapper);
        return skuInfoList;
    }

    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        try {
            return this.getSkuInfoRedis(skuId);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 从redis获取skuInfo
     *
     * @param skuId
     * @return
     */
    private SkuInfo getSkuInfoRedis(Long skuId) throws InterruptedException {
        SkuInfo skuInfo = null;
        try {
            // 缓存存储数据：key-value
            // 定义key sku:skuId:info
            String skuKey = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKUKEY_SUFFIX;
            // 获取里面的数据？ redis 有五种数据类型 那么我们存储商品详情 使用哪种数据类型？
            // 获取缓存数据
            skuInfo = (SkuInfo) redisTemplate.opsForValue().get(skuKey);
            // 如果从缓存中获取的数据是空
            if (skuInfo == null) {
                // 直接获取数据库中的数据，可能会造成缓存击穿。所以在这个位置，应该添加锁。
                // 第一种：redis ，第二种：redisson
                // 定义锁的key sku:skuId:lock  set k1 v1 px 10000 nx
                String lockKey = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKULOCK_SUFFIX;
                // 定义锁的值
                String uuid = UUID.randomUUID().toString().replace("-", "");
                // 上锁
                Boolean isExist = redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS);
                if (isExist) {
                    // 执行成功的话，则上锁。
                    log.info("获取到分布式锁");
                    // 真正获取数据库中的数据 {数据库中到底有没有这个数据 = 防止缓存穿透}
                    skuInfo = getSkuInfoDB(skuId);
                    // 从数据库中获取的数据就是空
                    if (skuInfo == null) {
                        // 为了避免缓存穿透 应该给空的对象放入缓存
                        SkuInfo skuInfo1 = new SkuInfo(); //对象的地址
                        redisTemplate.opsForValue().set(skuKey, skuInfo1, RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
                        return skuInfo1;
                    }
                    // 查询数据库的时候，有值
                    redisTemplate.opsForValue().set(skuKey, skuInfo, RedisConst.SKUKEY_TIMEOUT, TimeUnit.SECONDS);
                    // 解锁：使用lua 脚本解锁
                    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    redisTemplate.execute(new DefaultRedisScript(script, Long.class), Arrays.asList(lockKey), uuid);
                    return skuInfo;
                } else {
                    // 其他线程等待
                    Thread.sleep(100);
                    return getSkuInfo(skuId);
                }
            } else {
                return skuInfo;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 为了防止缓存宕机：从数据库中获取数据
        return getSkuInfoDB(skuId);
    }


    /**
     * redisson实现分布式锁，获取skuInfo
     *
     * @param skuId
     * @return SkuInfo
     */
    private SkuInfo getSkuInfoRedisson(Long skuId) {
        String skuKey = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKUKEY_SUFFIX;
        SkuInfo skuInfo = (SkuInfo) redisTemplate.opsForValue().get(skuKey);
        if (null == skuInfo) {
            String lockKey = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKULOCK_SUFFIX;
            RLock lock = this.redissonClient.getLock(lockKey);
            try {
                //尝试获取锁
                boolean result = lock.tryLock(RedisConst.SKULOCK_EXPIRE_PX1, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS);
                if (result) {
                    try {
                        skuInfo = this.getSkuInfoDB(skuId);
                        if (null == skuInfo) {
                            //防止缓存穿透
                            SkuInfo skuInfo1 = new SkuInfo();
                            redisTemplate.opsForValue().set(skuKey, skuInfo1, RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
                            return skuInfo1;
                        }
                        redisTemplate.opsForValue().set(skuKey, skuInfo, RedisConst.SKUKEY_TIMEOUT, TimeUnit.SECONDS);
                        return skuInfo;
                    } finally {
                        lock.unlock();
                    }
                } else {
                    //等待
                    try {
                        Thread.sleep(100);
                        return this.getSkuInfo(skuId);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        } else {
            return skuInfo;
        }
        return this.getSkuInfoDB(skuId);
    }

    /**
     * 查询数据库获取sku数据
     *
     * @param skuId
     * @return
     */
    private SkuInfo getSkuInfoDB(Long skuId) {
        SkuInfo skuInfo = this.baseMapper.selectById(skuId);
        if (null == skuInfo) return null;
        //查找sku图片
        LambdaQueryWrapper<SkuImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuImage::getSkuId, skuId);
        List<SkuImage> skuImages = this.skuImageMapper.selectList(wrapper);
        skuInfo.setSkuImageList(skuImages);
        return skuInfo;
    }

    /**
     * 查询实时价格
     *
     * @param skuId
     * @return
     */
    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        SkuInfo skuInfo = this.baseMapper.selectById(skuId);
        if (null != skuInfo) {
            return skuInfo.getPrice();
        }
        return new BigDecimal(0);
    }
}




