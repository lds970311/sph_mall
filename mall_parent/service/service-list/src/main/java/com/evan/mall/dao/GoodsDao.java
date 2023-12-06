package com.evan.mall.dao;

import com.evan.mall.list.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsDao extends ElasticsearchRepository<Goods, Long> {
}
