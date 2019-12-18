package com.leyou.controller;

import com.leyou.service.GoodsHtmlService;
import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.Map;

@Controller
public class GoodsController {


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;


    @RequestMapping("item/{sid}.html")
    public String findGoodsBySpuId(@PathVariable("sid") Long sid, Model model){
        System.out.println("ddd");
        Map<String, Object> goodsInfoBySpuId = goodsService.findGoodsInfoBySpuId(sid);
        model.addAllAttributes(goodsInfoBySpuId);
        goodsHtmlService.createHtml(sid);
        return "item";
    }

}
