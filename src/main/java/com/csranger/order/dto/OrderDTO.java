package com.csranger.order.dto;

import com.csranger.order.model.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 显然一次订单可能会购买许多商品，这就意味着：某个 orderId 对应的 订单Order 会有许多 订单商品详情orderId
 * (一对多关系：对象OrderDTO 封装1个 订单 及其对应的多个 订单商品详情)
 */
@Data
public class OrderDTO {

    private List<OrderDetail> orderDetailList;
    // orderMaster 属性
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    private Integer orderStatus;
    private Integer payStatus;

}
