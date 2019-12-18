package com.leyou.service.impl;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.item.pojo.*;
import com.leyou.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;

    @Override
    public Map<String, Object> findGoodsInfoBySpuId(Long spuId) {
        Map<String,Object> map=new HashMap<>();
        Spu spu=goodsClient.findSpuBySpuId(spuId);
        SpuDetail spuDetail=goodsClient.findAllSpuDetailBySpuId(spuId);
        List<Sku> skus=goodsClient.findAllSkusBySpuId(spuId);
        Brand brand=null;
        List<Map<String,Object>> categories=new ArrayList<>();
        List<SpecGroup> specGroup=null;
        Map<String,Object> specsMap=new HashMap<>();
        if(spu!=null) {
            brand = brandClient.findBrandByBid(spu.getBrandId());
            List<Long> cids= Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3());
            List<String> categoryNames = categoryClient.findCategoryNameByCids(cids);
            for (int i=0;i<cids.size();i++){
                Map<String,Object> cateMap=new HashMap<>();
                cateMap.put("id", cids.get(i).toString());
                cateMap.put("name", categoryNames.get(i));
                categories.add(cateMap);
            }
            specGroup = goodsClient.findAllSpecsByCid(spu.getCid3());
            //特殊规格参数
            List<SpecParam> specParams = goodsClient.findAllSpecParamByCondition(spu.getCid3(), null,false);
           specParams.forEach(specParam -> {
               specsMap.put(specParam.getId().toString(),specParam.getName());
           });
        }
        map.put("spu",spu);
        map.put("spuDetail",spuDetail);
        map.put("brand",brand);
        map.put("categories",categories);
        map.put("skus",skus);
        map.put("specGroup",specGroup);
        map.put("specsMap",specsMap);  //特殊规格参数
        return map;
    }
}
