package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RequestMapping("category")
public interface CategoryClientApi {


    @GetMapping
    public List<String> findCategoryNameByCids(@RequestParam("cids") List<Long> cids);

    @GetMapping("list")
    public List<Category> findAllByPid(@RequestParam(name="pid",required = true) Long pid);

    @GetMapping("bid/{bid}")
    public List<Category> findAllByBid(@PathVariable(name="bid",required = true) Long bid);

    @PostMapping
    public Void addCategory(Category category);

    @DeleteMapping("{id}")
    public Void deleteCategory(@PathVariable("id") Long id);

    @PutMapping
    public Void updateCategory(Category category);

}
