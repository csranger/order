package com.csranger.order.server.enums;

import lombok.Getter;

/**
 * payStatus 字段两个值使用 枚举类 表示而已
 */
@Getter
public enum PayStatusEnum {
    WAIT(0, "未支付"),
    SUCCESS(1, "已支付");

    private Integer code;
    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
