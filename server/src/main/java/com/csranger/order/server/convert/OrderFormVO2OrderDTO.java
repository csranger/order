package com.csranger.order.server.convert;

import com.csranger.order.server.VO.OrderCreateFormVO;
import com.csranger.order.server.dto.OrderDTO;
import com.csranger.order.server.exception.OrderException;
import com.csranger.order.server.model.OrderDetail;
import com.csranger.order.server.result.CodeMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderFormVO2OrderDTO {

    /**
     * 生成订单发来的表单信息 OrderCreateFormVO 对象 -> 订单信息 OrderDTO 对象用于生成订单
     */
    public static OrderDTO convert(OrderCreateFormVO orderCreateFormVO) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderCreateFormVO.getName());
        orderDTO.setBuyerPhone(orderCreateFormVO.getPhone());
        orderDTO.setBuyerAddress(orderCreateFormVO.getAddress());
        orderDTO.setBuyerOpenid(orderCreateFormVO.getOpenid());

        // items:      [{
        //        productId:          "18723423",
        //        productQuantity:    2
        //    }]
        // json 数据传来变成 String，使用 Gson 依赖将其恢复成 List<OrderDetail>
        List<OrderDetail> orderDetailList = new ArrayList<>();
        Gson gson = new Gson();
        // items 字符串的json数据还原成 OrderDetail 组成的 list
        try {
            orderDetailList = gson.fromJson(orderCreateFormVO.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (Exception e) {
            log.error("Json数据转换错误, String={}", orderCreateFormVO.getItems());
            throw new OrderException(CodeMsg.ORDER_JSON_DATA_CONVERT_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
