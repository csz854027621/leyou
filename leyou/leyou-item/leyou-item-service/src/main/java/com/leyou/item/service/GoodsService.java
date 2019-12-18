package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GoodsService {


    Spu findSpuBySpuId(Long spuId);

    /**
     * 规程参数组
     * @param gid
     * @return
     */
    List<SpecGroup> findAllSpecGroupByCondition(Long gid);

    /**
     * 规程参数组和规程参数
     * @param gid
     * @return
     */
    List<SpecGroup> findAllSpecsByCid(Long cid);
    PageResult<SpuBo> findAllSpuBoByCondition(String key, Boolean saleable,
                                            Integer page, Integer rows);

    List<SpecParam> findAllSpecParamByCondition(Long cid,Boolean search,Boolean generic);

    void save(SpuBo sb);

    SpuDetail findAllSpuDetailByCondition(Long spuId);

    List<Sku> findAllSkusByCondition(Long spuId);

    void update(SpuBo sb);
}
