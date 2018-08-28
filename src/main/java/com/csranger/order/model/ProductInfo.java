package com.csranger.order.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品对象
 * 来自 Product 应用
 */
//@Table(name = "product_info")   // 如果类名和表名不一致需要加上此注解；如果符合下划线变大写的规律可省略，这里省略
@Data
@Entity                           // 作用是将数据库表和这个对象对应起来，否则这个 ProductInfo 类只是一个单纯的类而已
public class ProductInfo {

    @Id                                       // 表示这个成员变量是表的主键
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productDescription;
    private String productIcon;
    private Integer productStatus;              // 商品状态 0-正常 1-下架
    private Integer categoryType;               // 类目编号 和 product_category 表关联
    private Date createTime;
    private Date updateTime;

}
