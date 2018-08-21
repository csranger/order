package com.csranger.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 把 RestTemplateConfig 作为一个 Bean 配置上去
 */
@Component
public class RestTemplateConfig {

    @Bean
    @LoadBalanced     // 和实现服务间的通信：RestTemplate 第二种方式本质相同，这里使用 @LoadBalanced 注解，而不是 loadBalancerClient
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
