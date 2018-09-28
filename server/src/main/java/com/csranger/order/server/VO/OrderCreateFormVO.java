package com.csranger.order.server.VO;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * com.csranger.order/create POST 生成订单请求需要的表单数据
 * 表单传递给这个类的对象，生成订单请求表单传递的属性，这里也需要对应的属性
 * 需要进行参数校验：同秒杀项目中的登陆校验
 */
@Data
public class OrderCreateFormVO {

    @NotEmpty(message = "姓名必填")   // @NotEmpty除了@NotNull之外还需要保证@Size(min=1)
    private String name;

    @NotEmpty(message = "手机必填")
    private String phone;

    @NotEmpty(message = "地址必填")
    private String address;

    @NotEmpty(message = "微信id必填")
    private String openid;

    // items:      [{
    //        productId:          "18723423",
    //        productQuantity:    2
    //    }]
    // json 数据传来变成 String，使用 Gson 依赖将其恢复成 List<OrderDetail>
    @NotEmpty(message = "购物车必填")
    private String items;
}
