package com.leyou.order.service.api;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-gateway", path = "/api/item")
/*extends GoodsApi*/
public interface GoodsService  {
}
