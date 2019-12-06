package com.leyou.controller;

import com.leyou.client.GoodsClient;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.pojo.Goods;
import com.leyou.pojo.SearchPageResult;
import com.leyou.pojo.SearchRequest;
import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.directory.SearchResult;
import javax.websocket.server.PathParam;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("page")
    public ResponseEntity<SearchPageResult> findAll(
            @RequestBody SearchRequest searchRequest
    ){
        SearchPageResult goodsByKey = goodsService.findGoodsByKey(searchRequest);
        if (CollectionUtils.isEmpty(goodsByKey.getItems())){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(goodsByKey);
    }

}
