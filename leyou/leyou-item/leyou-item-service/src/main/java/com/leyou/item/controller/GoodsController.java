package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService gs;


    /**
     * 根矩cid查询规格参数组
     * @param cid  商品类名id
     * @return
     */
    @GetMapping("spec/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> findAllSpecGroupByCondition(
            @PathVariable("cid") Long cid){

        List<SpecGroup> list=gs.findAllSpecGroupByCondition(cid);
        if(CollectionUtils.isEmpty(list)){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(list);
    }


    /**
     * 根据条件查询商品通用属性列表
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>>  findAllSpuBoByCondition(
            @RequestParam(name="key",required = false) String key,
            @RequestParam(name="saleable" ,required = false) Boolean saleable,
            @RequestParam(name="page" ,defaultValue = "1") Integer page,
            @RequestParam(name="rows",defaultValue = "5") Integer rows
    ){
        PageResult<SpuBo> result=gs.findAllSpuBoByCondition(key,saleable,page,rows);
        if (CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
    @GetMapping("spec/params")
    public ResponseEntity<List<SpecParam>> findAllSpecParamByCondition(@RequestParam("cid") Long cid){
        List<SpecParam> params = gs.findAllSpecParamByCondition(cid);

        if (CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

        @PostMapping("goods")
    public ResponseEntity<Void> save(@RequestBody SpuBo sb){
        gs.save(sb);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
