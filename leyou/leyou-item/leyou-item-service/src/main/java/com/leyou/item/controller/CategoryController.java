package com.leyou.item.controller;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryService;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    CategoryService cs;


    @GetMapping("list")
    public ResponseEntity<List<Category>> findAllByPid(@RequestParam(name="pid",required = true) Long pid){

        List<Category> allByPid = cs.findAllByPid(pid);
        if (CollectionUtils.isEmpty(allByPid)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(allByPid);
    }

    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> findAllByBid(@PathVariable(name="bid",required = true) Long bid){
        List<Category> oneByBid =  cs.findAllByBid(bid);
        if (oneByBid==null){
           return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(oneByBid);
    }

    @PostMapping
    public ResponseEntity<Void> addCategory(Category category){
        cs.add(category);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id){
        cs.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateCategory(Category category){
        cs.update(category);
        return ResponseEntity.ok().build();
    }

}
