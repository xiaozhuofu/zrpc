# zrpc
zrpc是一款自定义实现的企业级rpc框架,用于解决  
# 一、框架架构图
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/1-ZRPC%E6%A1%86%E6%9E%B6%E6%B1%87%E6%80%BB%E5%9B%BE.png)
# 二、代码架构整体设计
## 1.使用技术栈
明确技术栈: SpringBoot2.4.1+Netty4.1.25Final+JDK8+zookeeper3.5.8+Apache Curator2.12.0<br/>
项目依赖管理工具: maven3.6.1<br/>
代码版本控制工具: git
## 2.项目代码整体架构
### 2.1 项目结构
zrpc<br/>
 ├─zrpc-consumer  
 ├─zrpc-core  
 ├─zrpc-interface  
 ├─zrpc-protocol  
 ├─zrpc-provider  
 └─zrpc-registry  
### 2.2 项目模块
1、zrpc-core:基础类库，提供通用的工具及模型定义，底层基础包  
2、zrpc-protocol:网络通信模块，实现自定义协议，对协议的编解码，序列化及反序列化实现  
3、zrpc-registry:注册中心模块，实现服务注册及发现的功能，负载均衡的功能  
4、zrpc-provider:服务提供者模块，负责发布RPC服务，处理RPC请求  
5、zrpc-consumer:服务消费者模块，负责实现远程调用  
#### 2.3 各个模块的依赖关系图
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/2-%E6%A8%A1%E5%9D%97%E4%BE%9D%E8%B5%96%E5%85%B3%E7%B3%BB%E5%9B%BE.png)
