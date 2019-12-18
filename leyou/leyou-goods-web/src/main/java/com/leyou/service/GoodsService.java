package com.leyou.service;


import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.Spu;

import java.util.List;
import java.util.Map;

public interface GoodsService {

    /**
     * 通过spuId获取所需信息
     * @param spuId
     * @return
     */
    public Map<String,Object> findGoodsInfoBySpuId(Long spuId);





}
