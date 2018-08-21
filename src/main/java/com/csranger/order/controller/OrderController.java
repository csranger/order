package com.csranger.order.controller;

import com.csranger.order.VO.OrderCreateDetailVO;
import com.csranger.order.VO.OrderCreateFormVO;
import com.csranger.order.convert.OrderFormVO2OrderDTO;
import com.csranger.order.dto.OrderDTO;
import com.csranger.order.exception.OrderException;
import com.csranger.order.result.CodeMsg;
import com.csranger.order.result.Result;
import com.csranger.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/order")
@Slf4j        // lombok 中简化一条语句 private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderController.class);
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     *
     * 1. 参数校验
     * 2. 查询商品信息(调用商品服务)
     * 3. 计算总价
     * 4. 扣库存(调用商品服务)
     * 5. 订单入库
     */
    @PostMapping(value = "/create")
    public Result<OrderCreateDetailVO> create(@Valid OrderCreateFormVO orderCreateFormVO, BindingResult bindingResult) {
        // 1. 参数校验
        if (bindingResult.hasErrors()) {
            log.error("创建订单参数不正确, orderCreateFormVO={}", orderCreateFormVO);
            throw new OrderException(CodeMsg.ORDER_PARAMETER_ERROR);
        }
        // 5. 订单入库
        // 5.1 OrderCreateFormVO -> OrderDTO 用于生成订单
        OrderDTO orderDTO = OrderFormVO2OrderDTO.convert(orderCreateFormVO);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("创建订单信息为空");
            throw new OrderException(CodeMsg.ORDER_SHOPPING_CART_EMPTY);
        }
        // 5.2
        OrderDTO result = orderService.create(orderDTO);
        OrderCreateDetailVO orderCreateDetailVO = new OrderCreateDetailVO();  // 使用Map也可
        orderCreateDetailVO.setOrderId(result.getOrderId());

        return Result.success(orderCreateDetailVO);
    }
}
