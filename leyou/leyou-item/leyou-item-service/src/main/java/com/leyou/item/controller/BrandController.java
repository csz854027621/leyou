package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService bs;

    @GetMapping("bid/{bid}")
    public ResponseEntity<Brand> findBrandByBid(@PathVariable("bid") Long bid){
        Brand one = bs.findOneByBid(bid);
        if (one==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(one);
    }

    @RequestMapping("page")
    public ResponseEntity<PageResult<Brand>> findAllByCondition(
        @RequestParam(name="key",required = false)   String key,
        @RequestParam(name="page",defaultValue = "1") Integer page, //当前页数
        @RequestParam(name="rows",defaultValue = "5") Integer rows,
        @RequestParam(name="sortBy",required = false) String sortBy,
        @RequestParam(name="desc",required = false) Boolean desc

    ){
        PageResult<Brand> result= bs.findAllByCondition(key,page,rows,sortBy,desc);
        if(CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(result);
    }

    @RequestMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> findAllByCid(@PathVariable("cid") Long cid){
        List<Brand> list=bs.findAllByCid(cid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Void> save(Brand brand,@RequestParam(name="cids",required = true) List<Long> cids){
        bs.save(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping
    public ResponseEntity<Void> update(Brand brand,@RequestParam(name="cids",required = true) List<Long> cids){
        bs.update(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{bid}")
    public ResponseEntity<Void> delete(@PathVariable("bid") Long bid){
        bs.delete(bid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
