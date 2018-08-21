package com.csranger.order.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品详情
 * 与 product_info 区别在于 订单中的 价格 不一定等于商品价格，例如促销时会打折
 * 显然一次订单可能会购买许多商品，这就意味着：某个 orderId 对应的 订单Order 会有许多 订单商品详情orderId (一对多关系：对象OrderDTO 封装1个订单及其对应的多个订单商品详情)
 */
@Data
@Entity
public class OrderDetail {

    @Id
    private String detailId;            // 主键 订单商品id
    private String orderId;             // 外键 订单id
    private String productId;           // 商品 id
    private String productName;
    private BigDecimal productPrice;
    private Integer productQuantity;
    private String productIcon;
}
