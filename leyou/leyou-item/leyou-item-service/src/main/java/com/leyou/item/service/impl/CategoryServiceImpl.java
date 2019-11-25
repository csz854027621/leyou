package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper cm;

    @Override
    public List<Category> findAllByPid(Long pid) {
        Category category=new Category();
        category.setParentId(pid);
        return cm.select(category);
    }

    @Override
    public List<Category> findAllByBid(Long bid) {
        return  cm.findAllByBid(bid);
    }

    @Override
    public void add(Category category) {
        cm.insert(category);
    }

    @Override
    public void update(Category category) {
        cm.updateByPrimaryKeySelective(category);
    }

    @Override
    public void delete(Long id) {
        cm.deleteByPrimaryKey(id);
    }
}
