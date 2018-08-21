package com.csranger.order.exception;

import com.csranger.order.result.CodeMsg;
import com.csranger.order.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * 1. @ControllerAdvice 控制器增强:@ControllerAdvice注解内部使用@ExceptionHandler、@InitBinder、@ModelAttribute注解的方法应用到所
 * 有的 @RequestMapping注解的方法,当使用@ExceptionHandler最有用
 * 2. @ControllerAdvice + @ExceptionHandler 进行全局的 Controller 层异常处理
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)     // 表明拦截所有异常
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {

        if (e instanceof OrderException) {
            return Result.error(((OrderException) e).getCodeMsg());
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
