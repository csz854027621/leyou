package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

public interface BrandService {
    PageResult<Brand> findAllByCondition(String key, Integer page, Integer rows, String sortBy, Boolean desc);
    void save(Brand brand, List<Long> cids);
    public Brand findOneByBid(Long bid);

    void update(Brand brand, List<Long> cids);
    public void delete(Long bid);

    List<Brand> findAllByCid(Long cid);
}
