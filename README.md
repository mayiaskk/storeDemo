# storeDemo

> Java无框架实现的商城demo

**功能主要有：**

- 普通用户：注册，登录，购物车，订单，支付，分页
- 后台管理：商品分类管理，商品管理，订单管理

**主要用的技术有**：

mysql，redis，servlet，jsp，ajax等

**开发使用的软件版本**：

idea2018，sql8，tomcat8

**配置**:

数据库配置在src/c3p0-config.xml中，redis通过jedis连接，jedis配置一部分在JedisUtils工具类里面，一部分在servlet代码中。

