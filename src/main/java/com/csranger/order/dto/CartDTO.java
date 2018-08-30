package com.csranger.order.dto;

import lombok.Data;

/**
 * 创建订单时，订单服务会向商品服务请求扣库存，传来的参数就是一组 productId 和对应的 productQuantity 参数
 * 使用 CartDTO 封装这些传来的参数
 */
@Data
public class CartDTO {

    private String productId;

    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
