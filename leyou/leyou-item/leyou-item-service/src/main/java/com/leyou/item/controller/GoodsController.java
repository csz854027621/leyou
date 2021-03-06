package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.*;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService gs;

    /**
     * 通过cid 查询 groups 和 specs 规格参数组和规格参数
     * @param 通过cid
     * @return
     */
    @GetMapping("specs/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> findAllSpecsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> allSpecsByCid = gs.findAllSpecsByCid(cid);
        if (CollectionUtils.isEmpty(allSpecsByCid)||allSpecsByCid.size()==0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allSpecsByCid);
    }

    /**
     * 通过spuId 查询 spu
     * @param spuId
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> findSpuBySpuId(@PathVariable("id") Long spuId){

        
        Spu spuBySpuId = gs.findSpuBySpuId(spuId);
        if (spuBySpuId==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuBySpuId);
    }


    /**
     * 根据cid查询规格参数组
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

    /**
     * 根据cid、search查询规格参数
     * @param cid,search
     * @return
     */
    @GetMapping("spec/params")
    public ResponseEntity<List<SpecParam>> findAllSpecParamByCondition(
            @RequestParam("cid") Long cid,
            @RequestParam(value = "search",required = false) Boolean search,
            @RequestParam(value = "generic",required = false) Boolean generic
            ){
        List<SpecParam> params = gs.findAllSpecParamByCondition(cid,search,generic);

        if (CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

    /***
     * 根据spuId查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> findAllSpuDetailByCondition(@PathVariable("spuId") Long spuId){
        SpuDetail spuDetails=gs.findAllSpuDetailByCondition(spuId);
        if (spuDetails==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetails);
    }

    /**
     * 根据SpuId查询sku集合
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity< List<Sku>> findAllSkusByCondition(@RequestParam(name="id") Long id){
        List<Sku> skus=gs.findAllSkusByCondition(id);
        if (CollectionUtils.isEmpty(skus)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * 保存数据
     * @param sb
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> save(@RequestBody SpuBo sb){
        gs.save(sb);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改数据
     * @param sb
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> update(@RequestBody SpuBo sb){
        gs.update(sb);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }






}
