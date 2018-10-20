
# 项目过程
## 一、初始化
0. 新建项目是需要添加 Eureka Discover 依赖：作为 eureka client 
1. 在启动类上加上 @EnableDiscoveryClient 注解
2. 配置到 eureka： eureka.client.service-url.defaultZone=http://localhost:8080/eureka/
3. 添加 spring-boot-starter-web 依赖
4. 添加 spring-jps, mysql, lombok 依赖

## 二、服务拆分：订单服务(新建一个项目 com.csranger.order)
### 2.1 API 
1. POST /com.csranger.order/create
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
### 2.2 sql 创建表
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

## 三、应用通信
### 3.1 RestTemplate 三种使用方式
1. 启动 2 个 Product 应用实例，在配置复制一个 ProductApplication2 接口可设为 -Dserver.port=9080
2. Order 应用访问 Product 应用需要考虑负载均衡策略，以使得不要其中某个地址的 应用 负载太大，要均衡

### 3.2 负载均衡器 ribbon
1. alt+command+b 点击不会跳到接口，而是具体实现的方法处
2. 右键 Diagrams -> show Diagrams
3. 可以通过配置调整 Order 应用访问 Product 应用的负载均衡策略
4. eureka 属于客户端发现，它的负载均衡是软负载，客户端会向服务器 eureka server 拉取已注册的可用服务信息，然后根据负载均衡策略直接命中哪
    台服务器进行请求，整个过程均在客户端完成，不需要服务器参与。spring cloud 客户端负载均衡就是ribbon组件。
5. @LoadBalanced 添加后，ribbon会自动基于某种负载均衡策略(轮询，随机连接)去连接目标服务
6. 当有 2 个 Product 应用启动，查看 Order 应用访问哪个 Product 应用连接？可以在启动 ProductApplication1 时修改return "this is product's msg 1";
    启动 ProductApplication2 时修改成 return "this is product's msg 2" 然后多次在 Order 应用中访问可以发现返回值会变化，这是因为默认采用 轮询方式 
7. 如何改变负载均衡策略 http://cloud.spring.io/spring-cloud-static/Finchley.SR1/single/spring-cloud.html#_customizing_the_default_for_all_ribbon_clients
    
### 3.3 使用 Feign 进行应用通信
1. 添加依赖 spring-cloud-starter-feign 并在启动类上添加注解 @EnableFeignClients 
2. 新建 client 包，新建 ProductClient 接口
        ```
        @FeignClient(name = "product")
        public interface ProductClient {
            @GetMapping(value = "/msg")         // 访问 product 应用下的 msg 接口
            String productMsg();
    
        }
        ```
3. 在 ClientController 里就可以直接引入 ProductClient 的 Bean 访问 product 服务的 msg 接口
4. Feign:声明式REST客户端，采用基于接口的注解

5. 使用 Feign 查询商品信息
    - 订单服务调用商品服务查询商品信息

6. 使用 Feign 扣库存
    - 订单服务调用商品服务扣库存

7. 使用 Feign 扣库存
    - 订单服务调用商品服务扣库存

8. 完成下单流程

### 3.4 改造成多模块 
1. 商品服务供订单服务调用存在的问题
        - 直接暴露了与数据库对应的实体类
        - ProductController 类中的 listForOrder 方法直接返回 List<ProductInfo>
        - ProductInfo 与数据库相对应，这不安全
        - 订单服务与商品服务有许多重复类，比如CartDTO，ProductInfo 等类
        - ProductClient 里写了商品服务的一些接口，开发订单服务的可能不熟悉，放在订单服务不合适，应该放在商品服务以供调用，因为这两组服务可能有不同团队负责

2. 将订单模块和商品模块都改造成多模块解决上述问题
        - 订单服务页改造成多模块
        - 订单服务调用商品服务时需要在订单服务启动类上加上 @EnableFeignClients(basePackages = "com.csranger.product.client")
        - 订单服务需要在pom文件里引入商品服务的 product-client 的 jar 包，在商品服务里 mvn -Dmaven.test.skip=true -U clean install 将项目打包放在本地 .m2 路径下

### 3.5 rabbitmq docker Devops
1. rabbitmq消息队列 docker容器 Devops
    - 微服务和容器天生一对：从系统环境开始打包应用；轻量级对资源有效利用；可复用版本化整个应用环境
    - Devops
    
## 四、统一配置中心：order 服务作为 config client
### 4.1 config client
1. 在 order-server 的 pom 文件里引入 spring-cloud-config-client 依赖
2. 不需要像 eureka client 一样在启动类上加注解；order-server 的 properties 文件里进行配置
3. 需要将properties文件名改为 bootstrap.properties
4. config server 高可用
   - 启动多个实例，设置不同的端口即可，每当启动一个新的服务例如order，product，eureka会自动实现负载均衡，不会都访问单个 config server的
5. eureka 默认端口为8761，如果端口改成了8762，order如何改？
    - 首先需要将config-repo仓库里的order-dev.properties文件里的eureka修改成8762，但这是不够的，会报错
    - 这是因为order-server的配置文件设置要拿的配置文件是order-dev.properties，但config-server会先拿到order-dev.properties和
    order.properties两份配置文件，然后合并；这就意味这order.properties是放不同环境下(dev/test)的共同的配置的
   
### 4.2 spring cloud bus 自动刷新配置:这才体现出 config 的优势
1. 自动刷新配置理论
    - 使用消息队列来传递配置更改的消息
    - config server 使用了spring cloud bus 后对外提供一个http接口 /bus-refresh
    - 访问这个接口 config server 就会把更新配置的信息发送到mq中
    - 这样当修改放在远端git上的某个服务的配置后，就可以通过访问这个接口实现此服务配置的自动刷新
    - 此接口默认不对外暴露，需要在 config server 服务配置下
2. 在 config 服务添加 spring cloud bus 依赖，启动rabbitmq，启动config服务，在http://localhost:15672/#/queues可以看
   到spring cloud bus 创建的一个队列： Queue springCloudBus.anonymous.rBFG7Yv0T-6a_sEoCwHcxw
3. 在 order 服务 server 模块添加 spring cloud bus 依赖；此时 config server 与 order 均连接上消息队列了
4. 在 config server 上配置暴露全部接口: management.endpoints.web.exposure.include=*
    - 此时修改远端git上配置，向config server上的 /actuator/bus-refresh 服务发送 POST 请求即可刷新注册到config server 上的服务的配置，例如order服务
    - 在 order 服务上如果需要访问配置上数据，需要添加 @RefreshScope 注解，例如在 EnvController 类上
5. 缺点：每次在远端git上修改配置后需要向 config server上的 /actuator/bus-refresh 服务发送 POST 请求，麻烦
    - git 上配置 WebHooks 实现每次 push 代码后都会给远程 HTTP URL 发送一个 POST 请求
    - 这个请求地址就不是/bus-refresh，因为config server 向 webhooks 提供了一个专门的地址 /monitor
    - 这样只要远端git上配置变化，WebHooks会自动向config server发送一个post请求，然后注册到其上的服务就会自动刷新配置了，不再需要手动向/bus-refresh发送请求了




















