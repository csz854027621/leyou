package com.leyou.client;


import com.leyou.item.api.GoodsClientApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsClientApi {
}
