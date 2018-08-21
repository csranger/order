package com.csranger.order.enums;

import lombok.Getter;

/**
 * orderStatus 字段三个值使用 枚举类 表示而已
 */
@Getter
public enum OrderStatusEnum {

    NEW(0, "新订单"),
    FINISHED(1, "完结"),
    CANCLE(2, "取消");

    private Integer code;
    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
