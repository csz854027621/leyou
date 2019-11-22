package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

public interface BrandService {
    PageResult<Brand> findAllByCondition(String key, Integer page, Integer rows, String sortBy, Boolean desc);
}
