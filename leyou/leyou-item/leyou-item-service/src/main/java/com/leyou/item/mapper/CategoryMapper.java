package com.leyou.item.mapper;


import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.ids.SelectByIdsMapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {

    @Select("SELECT * FROM tb_category  WHERE  id IN (SELECT category_id FROM tb_category_brand WHERE brand_id=#{bid}) ")
    public List<Category> findAllByBid(Long bid);

    /**
     * 通过 cid1 cid2 cid3 直接查询商品类别信息
     * @param cid1
     * @param cid2
     * @param cid3
     * @return
     */
    @Select("select name from tb_category where id in (#{cid1},#{cid2},#{cid3}) ")
    public List<Category> findNameByCids(@Param("cid1") Long cid1,
                                         @Param("cid2")  Long cid2,
                                         @Param("cid3") Long cid3);

}
