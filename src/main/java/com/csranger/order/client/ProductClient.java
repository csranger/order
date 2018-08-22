package com.csranger.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping(value = "/msg")         // 访问 product 应用下的 msg 接口
    String productMsg();

}
