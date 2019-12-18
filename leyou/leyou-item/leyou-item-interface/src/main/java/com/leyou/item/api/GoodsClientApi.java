package com.leyou.item.api;


import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsClientApi {


    /**
     * 通过cid 查询 groups 和 specs 规格参数组和规格参数
     * @param 通过cid
     * @return
     */
    @GetMapping("specs/groups/{cid}")
    public List<SpecGroup> findAllSpecsByCid(@PathVariable("cid") Long cid);

    /**
     * 根据spuId查询spu
     * @param spuId
     * @return spu
     */
    @GetMapping("spu/{id}")
    public Spu findSpuBySpuId(@PathVariable("id") Long spuId);

    /**
     * 分页查询spu的list
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("spu/page")
    public PageResult<SpuBo> findAllSpuBoByCondition(
            @RequestParam(name="key",required = false) String key,
            @RequestParam(name="saleable" ,required = false) Boolean saleable,
            @RequestParam(name="page" ,defaultValue = "1") Integer page,
            @RequestParam(name="rows",defaultValue = "5") Integer rows
    );

    /**
     * 根据cid查询规格参数
     * @param cid
     * @return
     */
    @GetMapping("spec/params")
    public List<SpecParam> findAllSpecParamByCid(@RequestParam("cid") Long cid);

    /**
     * 根据spuId查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail findAllSpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 根据SpuId查询sku集合
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> findAllSkusBySpuId(@RequestParam(name="id") Long id);

    /**
     * 根据cid、search查询规格参数
     * @param cid,search
     * @return
     */
    @GetMapping("spec/params")
    public List<SpecParam> findAllSpecParamByCondition(
            @RequestParam("cid") Long cid,
            @RequestParam(value = "search",required = false) Boolean search,
            @RequestParam(value = "generic",required = false) Boolean generic);

}
