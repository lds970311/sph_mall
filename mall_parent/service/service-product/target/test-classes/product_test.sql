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