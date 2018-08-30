package com.csranger.order.service.impl;

import com.csranger.order.client.ProductClient;
import com.csranger.order.dao.OrderDetailDao;
import com.csranger.order.dao.OrderMasterDao;
import com.csranger.order.dto.CartDTO;
import com.csranger.order.dto.OrderDTO;
import com.csranger.order.enums.OrderStatusEnum;
import com.csranger.order.enums.PayStatusEnum;
import com.csranger.order.model.OrderDetail;
import com.csranger.order.model.OrderMaster;
import com.csranger.order.model.ProductInfo;
import com.csranger.order.service.OrderService;
import com.csranger.order.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterDao orderMasterDao;

    @Autowired
    private OrderDetailDao orderDetailDao;


    // 调用商品服务
    @Autowired
    private ProductClient productClient;

    /**
     * 创建订单，使用 insert sql
     * 在  order_master 和 order_detail 表插入订单数据 根据 OrderDTO 对象
     * <p>
     * 1. 参数校验
     * 2. 查询商品信息(调用商品服务)
     * 3. 计算总价
     * 4. 扣库存(调用商品服务)
     * 5. 订单入库:OrderDetail 和 OrderMaster 均需要
     */
    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();    // 生成一个随机的唯一的订单id
        // 查询商品信息(调用商品服务)
        List<String> productIdList = orderDTO.getOrderDetailList()
                .stream()                                         // 集合对象转化为一个流  OrderDetailList
                .map(OrderDetail::getProductId)                   // 流的遍历操作 每个操作的对象是 OrderDetail
                .collect(Collectors.toList());                    // map之后，用.collect(Collectors.toList())会得到操作后的集合
        List<ProductInfo> productInfoList = productClient.listForOrder(productIdList);

        // 计算总价 订单价格 * 购买数量
        BigDecimal sum = new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {   // 获取订单数量，一个订单会购买许多商品，所以遍历
            for (ProductInfo productInfo : productInfoList) {             // 获取商品价格
                if (orderDetail.getProductId().equals(productInfo.getProductId())) {
                    sum = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(sum);
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    // 订单详情(OrderDetail)入库
                    orderDetailDao.save(orderDetail);
                }
            }
        }
        // 减库存(调用商品服务)   先通过 orderDTO 获取 List<CartDTO>
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList()
                .stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(cartDTOList);


        // 订单主库(OrderMaster)入库：向 order_master 和 order_detail 表插入订单数据
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);    // 订单id使用随机唯一值
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(sum);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMasterDao.save(orderMaster);
        return orderDTO;
    }
}
