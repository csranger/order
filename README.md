
# 项目过程
## 初始化
0. 新建项目是需要添加 Eureka Discover 依赖
1. 在启动类上加上 @EnableDiscoveryClient 注解
2. 配置到 eureka： eureka.client.service-url.defaultZone=http://localhost:8080/eureka/
3. 添加 spring-boot-starter-web 依赖
4. 添加 spring-jps, mysql, lombok 依赖

## 订单服务(新建一个项目 order)
### API 
1. POST /order/create
2. 参数 
    ```
    name:       张三   
    phone:      "1782371231"
    address:    "XXXX"
    openid:     "....."
    items:      [{
        productId:          "18723423",
        productQuantity:    2
    }]
    ```
3. 返回
    ```
    {
       "code":  0,
       "msg": success,
       "data": {
           orderId: "12383242"
       }
    }
    ```
### sql 创建表
1. order_detail 表 【主键 detail_id + 外键 order_id】
    ```
    CREATE TABLE `order_detail` (
      `detail_id` varchar(32) NOT NULL DEFAULT '',
      `order_id` varchar(32) NOT NULL DEFAULT '',
      `product_id` varchar(32) NOT NULL DEFAULT '',
      `product_name` varchar(64) NOT NULL DEFAULT '' COMMENT '商品名称',
      `product_price` decimal(8,2) NOT NULL COMMENT '当前价格，单位分',
      `product_quantity` int(11) NOT NULL COMMENT '数量',
      `product_icon` varchar(512) DEFAULT '' COMMENT '小图',
      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      PRIMARY KEY (`detail_id`),
      KEY `idx_order_id` (`order_id`),
      CONSTRAINT `order_detail_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order_master` (`order_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单商品表';
    ```
2. order_master 表  【主键 order_id】
    ```
    CREATE TABLE `order_master` (
      `order_id` varchar(32) NOT NULL DEFAULT '',
      `buyer_name` varchar(32) NOT NULL DEFAULT '' COMMENT '买家名字',
      `buyer_phone` varchar(32) NOT NULL DEFAULT '' COMMENT '买家电话',
      `buyer_address` varchar(128) NOT NULL DEFAULT '' COMMENT '买家地址',
      `buyer_openid` varchar(64) NOT NULL DEFAULT '' COMMENT '买家微信openid',
      `order_amount` decimal(8,2) NOT NULL COMMENT '订单总金额',
      `order_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '订单状态，默认为新订单',
      `pay_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '支付状态，默认未支付',
      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
      PRIMARY KEY (`order_id`),
      KEY `idx_buyer_openid` (`buyer_openid`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';
    ``` 
3. model, dao, service, controller 层编码

## 应用通信
### RestTemplate 三种使用方式
1. 启动 2 个 Product 应用实例，在配置复制一个 ProductApplication2 接口可设为 -Dserver.port=9080
2. Order 应用访问 Product 应用需要考虑负载均衡策略，以使得不要其中某个地址的 应用 负载太大，要均衡

### 负载均衡器 ribbon
1. alt+command+b 点击不会跳到接口，而是具体实现的方法处
2. 右键 Diagrams -> show Diagrams
3. 可以通过配置调整 Order 应用访问 Product 应用的负载均衡策略
4. eureka 属于客户端发现，它的负载均衡是软负载，客户端会向服务器 eureka server 拉取已注册的可用服务信息，然后根据负载均衡策略直接命中哪
台服务器进行请求，整个过程均在客户端完成，不需要服务器参与。spring cloud 客户端负载均衡就是ribbon组件。
5. @LoadBalanced 添加后，ribbon会自动基于某种负载均衡策略(轮询，随机连接)去连接目标服务
6. 当有 2 个 Product 应用启动，查看 Order 应用访问哪个 Product 应用连接？可以在启动 ProductApplication1 时修改return "this is product's msg 1";
启动 ProductApplication2 时修改成 return "this is product's msg 2" 然后多次在 Order 应用中访问可以发现返回值会变化，这是因为默认采用 轮询方式 
7. 如何改变负载均衡策略
    - http://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#_customizing_the_default_for_all_ribbon_clients
8. 使用 Feign 进行应用通信
