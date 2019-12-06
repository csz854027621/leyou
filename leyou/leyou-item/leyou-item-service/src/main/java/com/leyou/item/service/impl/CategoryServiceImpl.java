package com.leyou.item.service.impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<String> findCategoryNameByCids(List<Long> cids) {
        List<Category> categories = cm.selectByIdList(cids);
        Stream<String> stringStream = categories.stream().map(category -> category.getName());
        return  stringStream.collect(Collectors.toList());
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
