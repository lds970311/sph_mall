package com.evan.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.evan.mall.common.constant.RedisConst;
import com.evan.mall.dao.GoodsDao;
import com.evan.mall.list.Goods;
import com.evan.mall.list.SearchAttr;
import com.evan.mall.list.SearchParam;
import com.evan.mall.list.SearchResponseVo;
import com.evan.mall.product.BaseAttrInfo;
import com.evan.mall.product.BaseCategoryView;
import com.evan.mall.product.BaseTrademark;
import com.evan.mall.product.SkuInfo;
import com.evan.mall.product.client.ProductFeignClient;
import com.evan.mall.service.SearchService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Autowired
    private GoodsDao goodsDao;

    @Qualifier("com.evan.mall.product.client.ProductFeignClient")
    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

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
        goods.setId(skuId);
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

    @Override
    public boolean lowerGoods(Long skuId) {
        if (skuId == null) {
            return false;
        }
        this.goodsDao.deleteById(skuId);
        return true;
    }

    /**
     * 更新商品热度
     *
     * @param skuId
     * @return
     */
    @Override
    public boolean incrHotScore(Long skuId) {
        if (skuId == null) {
            return false;
        }
        //累计数据
        ZSetOperations<Object, Object> zSetOperations = this.redisTemplate.opsForZSet();
        Double hotScore = zSetOperations.incrementScore(RedisConst.SKU_HOT_SCORE, "skuId:" + skuId, 1);
        //累计10次更新一次es
        if (null != hotScore && hotScore % 10 == 0) {
            //更新es
            this.updateHotScore(skuId, Math.round(hotScore));
        }
        return true;
    }


    private void updateHotScore(Long skuId, long round) {
        Optional<Goods> byId = this.goodsDao.findById(skuId);
        byId.ifPresent(goods -> {
            goods.setHotScore(round);
            this.goodsDao.save(goods);
        });
    }

    /**
     * 根据条件搜索商品
     *
     * @param searchParam ：用户传递的检索条件
     * @return ：返回检索结果
     */
    @SneakyThrows
    @Override
    public SearchResponseVo searchGoods(SearchParam searchParam) {
        //es搜索
        SearchRequest searchRequest = this.buildQueryDsl(searchParam);
        SearchResponse searchResponse = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return this.parseSearchResponseVo(searchResponse);
    }

    private SearchResponseVo parseSearchResponseVo(SearchResponse searchResponse) {
        return null;
    }

    private SearchRequest buildQueryDsl(SearchParam searchParam) {
        SearchRequest searchRequest = new SearchRequest("goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            MatchQueryBuilder title = QueryBuilders.matchQuery("title", searchParam.getKeyword())
                    .operator(Operator.AND);
            boolQueryBuilder.must(title);
        }
        //过滤品牌
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            if (split.length == 2) {
                TermQueryBuilder tmId = QueryBuilders.termQuery("tmId", split[0]);
                boolQueryBuilder.filter(tmId);
            }
        }
        //过滤分类
        if (null != searchParam.getCategory1Id()) {
            TermQueryBuilder category1Id = QueryBuilders.termQuery("category1Id", searchParam.getCategory1Id());
            boolQueryBuilder.filter(category1Id);
        }
        if (null != searchParam.getCategory2Id()) {
            TermQueryBuilder category2Id = QueryBuilders.termQuery("category2Id", searchParam.getCategory2Id());
            boolQueryBuilder.filter(category2Id);
        }
        if (null != searchParam.getCategory3Id()) {
            TermQueryBuilder category3Id = QueryBuilders.termQuery("category3Id", searchParam.getCategory3Id());
            boolQueryBuilder.filter(category3Id);
        }
        //过滤平台属性
        String[] props = searchParam.getProps(); //props=23:4G:运行内存
        if (null != props && props.length > 0) {
            for (String prop : props) {
                String[] split = prop.split(":");
                if (null != split && split.length == 3) {
                    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                    BoolQueryBuilder subBoolQuery = QueryBuilders.boolQuery();
                    TermQueryBuilder attrId = QueryBuilders.termQuery("attrs.attrId", split[0]);
                    TermQueryBuilder attrValue = QueryBuilders.termQuery("attrs.attrValue", split[1]);
                    boolQuery.must(attrValue);
                    boolQuery.must(attrId);
                    subBoolQuery.must(QueryBuilders.nestedQuery("attrs", boolQuery, ScoreMode.None));
                    boolQueryBuilder.filter(subBoolQuery);
                }
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        //分页
        searchSourceBuilder.from((searchParam.getPageNo() - 1) * searchParam.getPageSize());
        //排序
        String order = searchParam.getOrder();
        //1:hotScore 2:price; 1：综合排序/热度  2：价格
        if (!StringUtils.isEmpty(order)) {
            String[] strings = order.split(":");
            switch (strings[0]) {
                case "1":
                    searchSourceBuilder.sort("hotScore", "asc".equals(strings[1]) ? SortOrder.ASC : SortOrder.DESC);
                    break;
                case "2":
                    searchSourceBuilder.sort("price", "asc".equals(strings[1]) ? SortOrder.ASC : SortOrder.DESC);
                    break;
                default:
                    searchSourceBuilder.sort("hotScore", SortOrder.DESC);
            }
        } else {
            searchSourceBuilder.sort("hotScore", SortOrder.DESC);
        }
        //聚合
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg").field("tmId")
                .subAggregation(AggregationBuilders.terms("tmNameAgg").field("tmName").size(10))
                .subAggregation(AggregationBuilders.terms("tmLogoUrlAgg").field("tmLogoUrl").size(10));
        searchSourceBuilder.aggregation(tmIdAgg);

        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        attrAgg.subAggregation(attrIdAgg);
        attrIdAgg
                .subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(10))
                .subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(10));
        searchSourceBuilder.aggregation(attrAgg);

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("title")
                .preTags("<font color='red'>")
                .postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);
        //过滤字段
        searchSourceBuilder.fetchSource(new String[]{"id", "defaultImg", "title", "price"}, null);
        log.info("es请求已封装完成，结果：\n {}", searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }
}
