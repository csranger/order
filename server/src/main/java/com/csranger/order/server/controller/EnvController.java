package com.csranger.order.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查看当前配置文件是否是 dev 配置
 */
@RestController
@RequestMapping(value = "/env")
@RefreshScope
public class EnvController {

    @Value("${data}")    // 获取配置文件的 data 的值
    private String data;

    @GetMapping(value = "/print")
    public String print() {
        return data;
    }
}
