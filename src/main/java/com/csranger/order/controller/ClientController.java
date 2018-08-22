package com.csranger.order.controller;

import com.csranger.order.client.ProductClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实现服务间通讯
 * RPC      dubbo
 * HTTP     spring cloud  订单服务调用商品服务的接口
 */
@RestController
@Slf4j
public class ClientController {

    @Autowired
    private ProductClient productClient;

    @GetMapping(value = "/getProductMsg")
    public String getProductMsg() {
        String response = productClient.productMsg();
        log.info("response={}", response);
        return response;
    }
}
