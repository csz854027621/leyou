package com.leyou.item.service;

import com.leyou.item.pojo.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    /**
     * 根据父类pid查询信息
     * @param pid
     * @return
     */
    public List<Category> findAllByPid(Long pid);

    List<Category> findAllByBid(Long bid);

    List<String> findNameByCids(List<Long> cids);

    void add(Category category);

    void update(Category category);

    void delete(Long id);

}
