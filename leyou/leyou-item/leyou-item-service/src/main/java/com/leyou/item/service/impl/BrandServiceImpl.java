package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private  BrandMapper bm;

    @Override
    public PageResult<Brand> findAllByCondition(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        Example example=new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+ (desc ? "desc":"asc"));
        }
        PageHelper.startPage(page,rows);
        List<Brand> brands = bm.selectByExample(example);
        PageInfo pageInfo=new PageInfo<>(brands);
        PageResult<Brand> result=new PageResult<>();
        result.setItems(pageInfo.getList());
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Transactional
    @Override
    public void save(Brand brand, List<Long> cids) {

        bm.insertSelective(brand);
        cids.forEach(cid->bm.saveBrandAndCategory(brand.getId(),cid));

    }

    @Override
    public Brand findOneByBid(Long bid) {
        return bm.selectByPrimaryKey(bid);
    }

    @Transactional
    @Override
    public void update(Brand brand, List<Long> cids) {
        bm.updateByPrimaryKeySelective(brand);
        bm.deleteBrandAndCategoryByBid(brand.getId());
        cids.forEach(cid->bm.saveBrandAndCategory(brand.getId(),cid));
    }


    @Override
    public void delete(Long bid) {
        bm.deleteByPrimaryKey(bid);
    }
}
