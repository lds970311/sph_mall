# 1. 根据关键词匹配
POST /goods/_search
{
  "query": {
    "match": {
      "title": {
        "query": "小米手机",
        "operator": "and"
      }
    }
  }
}

# 2. 过滤查询，根据分类
POST /goods/_search
{
  "query": {
    "bool": {
      "filter": {
        "term": {
          "category3Id": "61"
        }
      }
    }
  }
}

# 3. 过滤查询，根据品牌
POST /goods/_search
{
  "query": {
    "bool": {
      "filter": {
        "term": {
          "tmId": "1"
        }
      }
    }
  }
}

# 4. 过滤查询，平台属性
POST /goods/_search
{
  "query": {
    "bool": {
      "must": {
        "nested": {
          "path": "attrs",
          "query": {
            "bool": {
              "filter": [
                {
                  "term": {
                    "attrs.attrId": "114"
                  }
                }
              ]
            }
          }
        }
      }
    }
  }
}

# 5.分页查询
GET /goods/_search
{
  "from": 5,
  "size": 5,
  "query": {
    "match_all": {}
  }
}

# 6. 排序查询
POST /goods/_search
{
  "from": 0,
  "size": 10,
  "sort": [
    {
      "price": {
        "order": "desc"
      },
      "hotScore": {
        "order": "desc"
      }
    }
  ],
  "query": {
    "match_all": {}
  }
}

# 7.聚合查询,品牌
POST /goods/_search
{
  "query": {
    "match_all": {}
  },
  "aggs": {
    "tmIdAgg": {
      "terms": {
        "field": "tmId"
      },
      "aggs": {
        "tmNameAgg": {
          "terms": {
            "field": "tmName"
          }
        }
      }
    }
  }
}

# 聚合查询,平台属性
POST /goods/_search
{
  "query": {
    "match_all": {}
  },
  "aggs": {
    "attrAgg": {
      "nested": {
        "path": "attrs"
      },
      "aggs": {
        "attrAgg": {
          "terms": {
            "field": "attrs.attrId"
          },
          "aggs": {
            "attrNameAgg": {
              "terms": {
                "field": "attrs.attrName"
              }
            },
            "attrValueAgg": {
              "terms": {
                "field": "attrs.attrValue"
              }
            }
          }
        }
      }
    }
  }
}

# 8.高亮查询
POST /goods/_search
{
  "query": {
    "match": {
      "title": "小米手机"
    }
  },
  "highlight": {
    "fields": {
      "title": {}
    },
    "pre_tags": [
      "<span style='color:red'>"
    ],
    "post_tags": [
      "</span>"
    ]
  }
}

# 9.过滤查询结果
POST /goods/_search
{
  "query": {
    "match_all": {
    }
  },
  "_source": [
    "id",
    "defaultImg",
    "title",
    "price"
  ]
}

# 10.组合
POST /goods/_search
{
  "from": 0,
  "size": 10,
  "sort": [
    {
      "price": {
        "order": "desc"
      },
      "hotScore": {
        "order": "desc"
      }
    }
  ],
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": {
              "query": "小米",
              "operator": "and"
            }
          }
        }
      ],
      "filter": [
        {
          "term": {
            "category3Id": "61"
          }
        },
        {
          "term": {
            "tmId": "1"
          }
        },
        {
          "bool": {
            "must": {
              "nested": {
                "path": "attrs",
                "query": {
                  "bool": {
                    "filter": [
                      {
                        "term": {
                          "attrs.attrValue": "512G"
                        }
                      }
                    ]
                  }
                }
              }
            }
          }
        }
      ]
    }
  },
  "aggs": {
    "tmIdAgg": {
      "terms": {
        "field": "tmId"
      },
      "aggs": {
        "tmNameAgg": {
          "terms": {
            "field": "tmName",
            "size": 10
          }
        },
        "tmLogoUrlAgg": {
          "terms": {
            "field": "tmLogoUrl",
            "size": 10
          }
        }
      }
    },
    "attrAgg": {
      "nested": {
        "path": "attrs"
      },
      "aggs": {
        "attrAgg": {
          "terms": {
            "field": "attrs.attrId",
            "size": 10
          },
          "aggs": {
            "attrNameAgg": {
              "terms": {
                "field": "attrs.attrName",
                "size": 10
              }
            },
            "attrValueAgg": {
              "terms": {
                "field": "attrs.attrValue",
                "size": 10
              }
            }
          }
        }
      }
    }
  },
  "highlight": {
    "fields": {
      "title": {}
    },
    "pre_tags": [
      "<span style='color:red'>"
    ],
    "post_tags": [
      "</span>"
    ]
  },
  "_source": [
    "id",
    "defaultImg",
    "title",
    "price"
  ]
}

GET /goods/_search
{
  "suggest": {
    "title_suggest": {
      "text": "小米",
      // 关键字
      "completion": {
        "field": "title",
        // 补全查询的字段
        "skip_duplicates": true,
        // 跳过重复的
        "size": 10
        // 获取前10条结果
      }
    }
  }
}

GET /goods