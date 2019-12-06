package com.leyou.client;

import com.leyou.item.api.CategoryClientApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryClientApi {

}
