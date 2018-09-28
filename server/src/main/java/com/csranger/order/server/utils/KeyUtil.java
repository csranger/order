package com.csranger.order.server.utils;

import java.util.Random;

public class KeyUtil {

    /**
     * 生成唯一主键
     * 不好，大并发下无法保证唯一，使用snowflake算法
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;  // 随机 6 位数：[100000, 1000000) = [0, 900000) + 100000
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
