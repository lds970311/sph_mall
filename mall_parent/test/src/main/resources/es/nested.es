# 步骤1：建立一个index
PUT my_index/_doc/1
{
  "group": "fans",
  "user": [
    {
      "first": "John",
      "last": "Smith"
    },
    {
      "first": "Alice",
      "last": "White"
    }
  ]
}

# 步骤2 : 执行查询
GET my_index/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "user.first": "Alice"
          }
        }
      ]
    }
  }
}

DELETE /my_index

# 步骤4：建立一个nested 类型的

PUT my_index
{
  "mappings": {
    "properties": {
      "user": {
        "type": "nested"
      }
    }
  }
}

# user字段映射为nested类型，而不是默认的object类型

# 5. 重新执行步骤1，使用nested 查询
GET /my_index/_search
{
  "query": {
    "nested": {
      "path": "user",
      "query": {
        "bool": {
          "must": [
            {
              "match": {
                "user.first": "Alice"
              }
            },
            {
              "match": {
                "user.last": "White"
              }
            }
          ]
        }
      }
    }
  }
}


GET /goods









