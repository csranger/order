package com.csranger.order.client;

import com.csranger.order.model.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping(value = "/msg")                             // 访问 product 应用下的 msg 接口
    String productMsg();

    @PostMapping(value = "/product/listForOrder")    // 访问 product 应用下的 /product/listForOrder 接口
    List<ProductInfo> listForOrder(@RequestBody List<String> productIdList);  // @RequestBody 视为处理传过来的参数是一个json字符串，要求请求必须是POST

}
