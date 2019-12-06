package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("brand")
public interface BrandClientApi {

    @GetMapping("bid/{bid}")
    public Brand findBrandByBid(@PathVariable("bid") Long bid);

    @RequestMapping("page")
    public PageResult<Brand> findAllByCondition(
            @RequestParam(name="key",required = false)   String key,
            @RequestParam(name="page",defaultValue = "1") Integer page, //当前页数
            @RequestParam(name="rows",defaultValue = "5") Integer rows,
            @RequestParam(name="sortBy",required = false) String sortBy,
            @RequestParam(name="desc",required = false) Boolean desc

    );

    @RequestMapping("cid/{cid}")
    public List<Brand> findAllByCid(@PathVariable("cid") Long cid);

    @PostMapping
    public Void save(Brand brand,@RequestParam(name="cids",required = true) List<Long> cids);

    @PutMapping
    public Void update(Brand brand,@RequestParam(name="cids",required = true) List<Long> cids);

    @DeleteMapping("{bid}")
    public Void delete(@PathVariable("bid") Long bid);


}