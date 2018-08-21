package com.csranger.order.service.impl;

import com.csranger.order.dao.OrderDetailDao;
import com.csranger.order.dao.OrderMasterDao;
import com.csranger.order.dto.OrderDTO;
import com.csranger.order.enums.OrderStatusEnum;
import com.csranger.order.enums.PayStatusEnum;
import com.csranger.order.model.OrderMaster;
import com.csranger.order.service.OrderService;
import com.csranger.order.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterDao orderMasterDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    /**
     * 创建订单，使用 insert sql
     * 在  order_master 和 order_detail 表插入订单数据 根据 OrderDTO 对象
     */
    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        // 查询商品信息
        // 计算总价
        // 减库存

        // 订单入库：向 order_master 和 order_detail 表插入订单数据
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(KeyUtil.genUniqueKey());    // 订单id使用随机唯一值
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(new BigDecimal(5));
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMasterDao.save(orderMaster);
        return orderDTO;
    }
}
