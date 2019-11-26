package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.SpuDetail;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GoodsService {

    List<SpecGroup> findAllSpecGroupByCondition(Long gid);
    PageResult<SpuBo> findAllSpuBoByCondition(String key, Boolean saleable,
                                            Integer page, Integer rows);

    List<SpecParam> findAllSpecParamByCondition(Long cid);

    void save(SpuBo sb);

    SpuDetail findAllSpuDetailByCondition(Long spuId);

    List<Sku> findAllSkusByCondition(Long spuId);

    void update(SpuBo sb);
}
