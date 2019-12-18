package com.leyou.listener;

import com.leyou.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class GoodsHtmlListener {

    @Autowired
    private  GoodsHtmlService goodsHtmlService;

    @RabbitListener(bindings =@QueueBinding(
            value=@Queue(value="leyou.html",durable ="true"),
            exchange = @Exchange(
                    value = "csz_exchange",ignoreDeclarationExceptions = "true", //路由名称
                    type = ExchangeTypes.TOPIC    //消息模型
            ),
            key = "item.#"    //映射名称
    ))
    public void updateData(Long spuId) throws IOException {
        File file=new File("D:\\Desktop\\nginx-1.14.0\\html\\item\\" + spuId + ".html");
        file.deleteOnExit();
        System.out.println("update成功！");
        goodsHtmlService.createHtml(spuId);
    }


}
