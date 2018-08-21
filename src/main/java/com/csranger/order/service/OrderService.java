package com.csranger.order.service;

import com.csranger.order.dto.OrderDTO;

public interface OrderService {

    /**
     * 插入：创建订单，使用 insert sql
     * 在  order_master 和 order_detail 表插入订单数据 根据 OrderDTO 对象
     */
    OrderDTO create(OrderDTO orderDTO);
}
