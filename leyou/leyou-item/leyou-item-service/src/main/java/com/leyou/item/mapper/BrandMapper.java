package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import javax.persistence.ExcludeSuperclassListeners;
import java.util.List;

public interface BrandMapper extends Mapper<Brand> {


    @Select("INSERT INTO tb_category_brand(category_id,brand_id) values(#{cid},#{bid}) ")
    public void saveBrandAndCategory(@Param("bid") Long bid,@Param("cid") Long cid);


    @Delete("delete from tb_category_brand where brand_id=#{bid} ")
    public void deleteBrandAndCategoryByBid(Long bid);

    @Select("SELECT * FROM tb_category_brand a INNER JOIN tb_brand b ON a.brand_id=b.id WHERE a.category_id=#{cid}")
    public List<Brand>  findAllByCid(Long cid);



}
