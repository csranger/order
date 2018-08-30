package com.csranger.order.controller;

import com.csranger.order.client.ProductClient;
import com.csranger.order.dto.CartDTO;
import com.csranger.order.model.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 调用商品服务
 * 实现服务间通讯
 * RPC      dubbo
 * HTTP     spring cloud  订单服务调用商品服务的接口
 */
@RestController
@Slf4j
public class ClientController {

    @Autowired
    private ProductClient productClient;

    /**
     * 测试调用商品服务的 /msg 接口
     */
    @GetMapping(value = "/getProductMsg")
    public String getProductMsg() {
        String response = productClient.productMsg();
        log.info("response={}", response);
        return response;
    }

    /**
     * 创建订单前调用商品服务查询商品信息
     */
    @GetMapping(value = "/getProductList")
    public String getProductList() {
        List<ProductInfo> productInfoList = productClient.listForOrder(Arrays.asList("157875196366160062", "157875196366160062"));
        log.info("response={}", productInfoList);
        return "ok";
    }

    /**
     * 创建订单时需要调用商品服务减库存
     */
    @GetMapping(value = "/reduceStock")
    public String reduceStock() {
        productClient.decreaseStock(Arrays.asList(new CartDTO("157876323366164068", 5)));
        return "ok";
    }
}
