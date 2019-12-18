package com.leyou.listener;

import com.leyou.client.GoodsClient;
import com.leyou.item.pojo.Spu;
import com.leyou.pojo.Goods;
import com.leyou.repository.GoodsRepository;
import com.leyou.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoodsListener {

    @Autowired
    private  GoodsService goodsService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @RabbitListener(bindings =@QueueBinding(
             value=@Queue(value="leyou.search",durable ="true"),
            exchange = @Exchange(
                    value = "csz_exchange",ignoreDeclarationExceptions = "true", //路由名称
                    type = ExchangeTypes.TOPIC    //消息模型
            ),
            key = "item.#"    //映射名称
    ))
    public void updateData(Long spuId) throws IOException {
        Goods goods = goodsService.buildGoodsBySpuBo(spuId);
        goodsRepository.save(goods);

    }



}
