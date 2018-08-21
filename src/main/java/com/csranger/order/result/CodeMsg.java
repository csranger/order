package com.csranger.order.result;

import lombok.Getter;

/**
 * 代表异常信息
 */
@Getter
public class CodeMsg {

    private Integer code;

    private String msg;


    // 结果正常是 CodeMsg 对象
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");

    // 通用异常 5001XX
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");



    // 订单异常 5002XX
    public static CodeMsg ORDER_PARAMETER_ERROR = new CodeMsg(500200, "创建订单参数不正确");
    public static CodeMsg ORDER_JSON_DATA_CONVERT_ERROR = new CodeMsg(500201, "Json数据转换错误");
    public static CodeMsg ORDER_SHOPPING_CART_EMPTY = new CodeMsg(500202, "创建订单信息为空");





    // 私有构造器
    private CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // toString
    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
