package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService bs;

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


}
