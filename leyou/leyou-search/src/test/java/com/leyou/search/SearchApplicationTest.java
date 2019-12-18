package com.leyou.search;

import com.leyou.repository.GoodsRepository;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.pojo.Goods;
import com.leyou.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchApplicationTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void find(){
        System.out.println(categoryClient.findCategoryNameByCids(
                Arrays.asList(1l,2l,3l)));
    }


    @Test
    public void createdElasticsearchList() {
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);

    }

    @Test
    public void initElasticsearchData() {
        int page = 1;
        int row = 100;
        PageResult<SpuBo> result;
        do {

            result = goodsClient.findAllSpuBoByCondition(null, null, page, row);
            List<SpuBo> items = result.getItems();
            List<Goods> goodsList = items.stream().map(spuBo -> {
                try {
                    return goodsService.buildGoodsBySpuBo(spuBo.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            goodsRepository.saveAll(goodsList);
            page++;
            row = items.size();
        } while (row == 100);


    }




    @Test
    public void getGoodsInfo() {
        PageResult<SpuBo> all =
                goodsClient.findAllSpuBoByCondition(null, null, 1, 3);
        System.out.println(all);
    }


    @Test
    public void demo(){
        List<Integer> list=Arrays.asList(1,2,3,4,5,6,7);
        list.forEach(one->{
            System.out.print(one+"-");   //循环遍历
        });
        System.out.println("");
        List<String> collect = list.stream().map(a -> {
            return a + "改";  //把集合里的每数据取出来， a+改    也就是字符串
        }).collect(Collectors.toList());
        collect.forEach(one->{
            System.out.print(one+"-");
        });
    }

}
