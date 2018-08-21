package com.csranger.order.dao;

import com.csranger.order.OrderApplicationTests;
import com.csranger.order.enums.OrderStatusEnum;
import com.csranger.order.enums.PayStatusEnum;
import com.csranger.order.model.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@Component
public class OrderMasterDaoTest extends OrderApplicationTests {

    @Autowired
    private OrderMasterDao orderMasterDao;

    /**
     * insert : 向 order_master 表中插入一条数据
     */
    @Test
    public void testSave() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setBuyerName("long");
        orderMaster.setBuyerPhone("18675467874");
        orderMaster.setBuyerAddress("Shanghai");
        orderMaster.setBuyerOpenid("10101010");
        orderMaster.setOrderAmount(new BigDecimal(2.3));
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderId("123456789");
        // order_master 表中的时间 timestamp 不需要设置自动生成

        OrderMaster result = orderMasterDao.save(orderMaster);
        Assert.assertTrue(result != null);
    }

}