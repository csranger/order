package com.csranger.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 实现服务间通讯
 * RPC      dubbo
 * HTTP     spring cloud  订单服务调用商品服务的接口
 */
@RestController
@Slf4j
public class ClientController {

//    @Autowired
//    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/getProductMsg")
    public String getProductMsg() {

        // 1. 实现服务间的通信：RestTemplate 第一种方式
        // 不行，url 写死，直接使用 restTemplate
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject("http://localhost:8080/msg", String.class);// String.class 是返回值类型
//        log.info("response={}", response);
//        return response;

        // 2. 实现服务间的通信：RestTemplate 第二种方式
        // 利用 loadBalancerClient 通过应用名过去 url，然后使用 RestTemplate
//        ServiceInstance serviceInstance = loadBalancerClient.choose("Product");  // eureka 上注册的服务名
//        String url = String.format("http://%s:%s/msg", serviceInstance.getHost(), serviceInstance.getPort());
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject(url, String.class);// String.class 是返回值类型
//        log.info("response={}", response);
//        return response;

        // 3. 实现服务间的通信：RestTemplate 第三种方式
        // 本质和第二种一样，不使用 loadBalancerClient 而是利用  @LoadBalanced  生成  RestTemplate 的 Bean
        String response = restTemplate.getForObject("http://product/msg", String.class);
        log.info("response={}", response);
        return response;
    }
}
