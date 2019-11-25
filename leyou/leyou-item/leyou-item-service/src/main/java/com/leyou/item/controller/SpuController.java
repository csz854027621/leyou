package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.service.SpuService;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("spu")
public class SpuController {

    @Autowired
    private SpuService spuService;


    @RequestMapping("page")
    public ResponseEntity<PageResult<SpuBo>>  findAllByCondition(
            @RequestParam(name="key",required = false) String key,
            @RequestParam(name="saleable" ,required = false) Boolean saleable,
            @RequestParam(name="page" ,defaultValue = "1") Integer page,
            @RequestParam(name="rows",defaultValue = "5") Integer rows
            ){
        PageResult<SpuBo> result=spuService.findAllByCondition(key,saleable,page,rows);
        if (CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

}
