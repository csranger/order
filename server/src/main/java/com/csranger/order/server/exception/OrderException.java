package com.csranger.order.server.exception;

import com.csranger.order.server.result.CodeMsg;
import lombok.Getter;

/**
 * 订单异常
 * 异常信息CodeMsg 5002XX
 */
@Getter
public class OrderException extends RuntimeException {

    private CodeMsg codeMsg;

    public OrderException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }
}
