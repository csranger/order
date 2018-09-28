package com.csranger.order.server.result;

import lombok.Getter;

/**
 * controller 层返回的对象
 */
@Getter     // lombok 自动生成 getter
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;


    // 使用两个静态方法创建 Result 对象
    // 成功时调用：只需传入 T data     成功的信息
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }
    // 构造器,使用上面静态方法获取对象，设为private
    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }



    // 失败时调用：只需传入 CodeMsg    CodeMsg 包括 code 和 msg
    public static <T> Result<T> error(CodeMsg cm) {
        return new Result<T>(cm);
    }
    // 构造器,使用上面静态方法获取对象，设为private
    private Result(CodeMsg cm) {
        if (cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }
}
