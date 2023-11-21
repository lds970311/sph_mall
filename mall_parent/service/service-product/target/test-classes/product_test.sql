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

# 根据spuid获取销售属性与销售属性值
select ssa.id,
       ssa.spu_id spu_id,
       ssa.base_sale_attr_id,
       ssav.id    spu_sale_attr_value_id,
       ssa.sale_attr_name,
       ssav.sale_attr_value_name
from spu_sale_attr ssa
         inner join spu_sale_attr_value ssav on ssa.base_sale_attr_id = ssav.base_sale_attr_id
    and ssa.spu_id = ssav.spu_id
where ssa.spu_id = 8;

select bai.id,
       bai.attr_name,
       bai.category_id,
       bai.category_level,
       bav.id attr_value_id,
       bav.value_name,
       bav.attr_id
from base_attr_info bai
         inner join base_attr_value bav on bai.id = bav.attr_id
WHERE
    # (bai.category_level = 1 and bai.category_id = 2)
    (bai.category_level = 2 and bai.category_id = 13)
# or (bai.category_level = 3 and bai.category_id = 61)
order by bai.category_level, bai.id;