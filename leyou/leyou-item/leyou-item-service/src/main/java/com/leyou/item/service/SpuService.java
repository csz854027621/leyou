package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;

public interface SpuService {


    PageResult<SpuBo> findAllByCondition(String key, Boolean saleable, Integer page, Integer rows);
}
