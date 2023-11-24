select bai.id,
       bai.attr_name,
       bai.category_id,
       bai.category_level,
       bav.id attr_value_id,
       bav.value_name,
       bav.id
from base_attr_info bai
         inner join base_attr_value bav
                    on bai.id = bav.attr_id
where (bai.category_level = 1 and bai.category_id = 1)
   or (bai.category_level = 2 and bai.category_id = 13)
   or (bai.category_id = 3 and bai.category_id = 61);

#
-- 根据spuid获取销售属性与销售属性值
select ssa.id,
       ssa.spu_id                    spu_id,
       ssa.base_sale_attr_id,
       ssav.id                       spu_sale_attr_value_id,
       ssa.sale_attr_name,
       ssav.sale_attr_value_name,
       if(skav.sku_id is null, 0, 1) is_checked
from spu_sale_attr ssa
         inner join spu_sale_attr_value ssav on ssa.base_sale_attr_id = ssav.base_sale_attr_id
    and ssa.spu_id = ssav.spu_id
         left join sku_sale_attr_value skav
                   on skav.sale_attr_value_id = ssav.id
                       and skav.sku_id = 27
where ssa.spu_id = 13
order by ssa.base_sale_attr_id, ssav.id;

select bai.id,
       bai.attr_name,
       bai.category_id,
       bai.category_level,
       bav.id attr_value_id,
       bav.value_name,
       bav.attr_id
from base_attr_info bai
         inner join base_attr_value bav on bai.id = bav.attr_id
WHERE #(bai.category_level = 1 and bai.category_id = 2) (bai.category_level = 2 and bai.category_id = 13)
# or (bai.category_level = 3 and bai.category_id = 61)
order by bai.category_level, bai.id;


# 创建商品分类视图

create view base_category_view2 as
select c3.id   as id,
       c1.id   as category1_id,
       c1.name as category1_name,
       c2.id   as category2_id,
       c2.name as category2_name,
       c3.id   as category3_id,
       c3.name as category3_name
from base_category1 c1
         inner join base_category2 c2 on c2.category1_id = c1.id
         inner join base_category3 c3 on c3.category2_id = c2.id;


-- 获取说个销售属性值和sku的对应关系
select group_concat(skav.sale_attr_value_id order by ssav.base_sale_attr_id separator '|'),
       skav.sku_id
from sku_sale_attr_value skav
         inner join spu_sale_attr_value ssav on skav.sale_attr_value_id = ssav.id
where skav.spu_id = 13
group by sku_id;

-- 查询sku平台属性
select temp.id          id,
       attr_name,
       category_id,
       category_level,
       temp.attr_id  as attr_id,
       temp.value_id as value_id,
       value_name,
       sku_id
from (select bai.id,
             bai.attr_name,
             category_id,
             category_level,
             bav.id value_id,
             value_name,
             attr_id
      from base_attr_info bai
               inner join base_attr_value bav on bav.attr_id = bai.id) temp
         inner join sku_attr_value sav on sav.value_id = temp.value_id and sav.attr_id = temp.attr_id
where sku_id = 23;