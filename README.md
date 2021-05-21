# zrpc
zrpc是一款自定义rpc框架,包含的模块具体有：  
zrpc-core【基础类库模块】、zrpc-protocol【网络通信模块】、zrpc-registry【注册中心模块】、zrpc-provider【服务提供者模块】、zrpc-consumer【服务消费者模块】
# 一、框架架构图
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/1.ZRPC%E6%A1%86%E6%9E%B6%E6%B1%87%E6%80%BB%E5%9B%BE.png)
# 二、代码架构整体设计
## 1.使用技术栈
明确技术栈: SpringBoot2.4.1+Netty4.1.25Final+JDK8+zookeeper3.5.8+Apache Curator2.12.0  
项目依赖管理工具: maven3.6.1  
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
1、zrpc-core：基础类库模块，提供通用的工具及模型定义，底层基础包  
2、zrpc-protocol：网络通信模块，实现自定义协议，对协议的编解码，序列化及反序列化实现  
3、zrpc-registry：注册中心模块，实现服务注册及发现的功能，负载均衡的功能  
4、zrpc-provider：服务提供者模块，负责发布RPC服务，处理RPC请求  
5、zrpc-consumer：服务消费者模块，负责实现远程调用  
### 2.3 各个模块的依赖关系图
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/2.%E6%A8%A1%E5%9D%97%E4%BE%9D%E8%B5%96%E5%85%B3%E7%B3%BB%E5%9B%BE.png)
## 3.项目子模块具体实现
### 3.1 zrpc-core 
【实现功能】提供基础的一些类及工具类，提供给其他模块使用  
封装请求类：RpcRequest，封装消费端给服务端发送的远程调用请求信息  
封装响应类：RpcResponse，封装服务端的响应消息  
封装服务元数据类：ServiceMeta，用来描述服务状态  
封装请求异步结果类：RpcFuture，自定义异步调用  
工具类：RpcUtils  
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/3.zrpc-core%E4%BB%A3%E7%A0%81%E5%85%B7%E4%BD%93%E8%AE%BE%E8%AE%A1.png)
### 3.2 zrpc-protocol
【实现功能】协议模块，实现自定义协议+编解码+序列化
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/4.zrpc-protocol%E4%BB%A3%E7%A0%81%E5%85%B7%E4%BD%93%E8%AE%BE%E8%AE%A1.png)
### 3.3 zrpc-registry
【实现功能】：注册中心，便于进行服务进行注册和发现
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/5.zrpc-registry%E4%BB%A3%E7%A0%81%E5%85%B7%E4%BD%93%E8%AE%BE%E8%AE%A1.png)
#### 1.负载均衡模块
基于负载均衡的策略，选择一个合适的服务节点返回给调用方
##### 1.1 Hash算法
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/6.hash%E7%AE%97%E6%B3%95.png)
##### 1.2 一致性Hash算法(虚拟节点)
本框架采用一致性Hash算法（虚拟节点），实现服务器压力的分摊
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/7.%E4%B8%80%E8%87%B4%E6%80%A7hash%E7%AE%97%E6%B3%95.png)
### 3.4 zrpc-interface
公共接口，用于消费者调用提供者
### 3.5 zrpc-provider
#### 1.实现功能模块介绍
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/8.zrpc-provider%E5%8A%9F%E8%83%BD%E6%A8%A1%E5%9D%97%E4%BB%8B%E7%BB%8D.png)
#### 2.代码具体设计
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/9.zrpc-provider%E4%BB%A3%E7%A0%81%E5%B1%82%E5%85%B7%E4%BD%93%E8%AE%BE%E8%AE%A1.png)
### 3.6 zrpc-consumer
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/10.zrpc-consumer%E4%BB%A3%E7%A0%81%E5%B1%82%E5%85%B7%E4%BD%93%E8%AE%BE%E8%AE%A1.png)

## 4.zrpc框架各模块实现架构图
![](https://github.com/xiaozhuofu/zrpc/blob/master/images/11.zprc%E6%A1%86%E6%9E%B6%E5%90%84%E6%A8%A1%E5%9D%97%E6%B1%87%E6%80%BB%E5%9B%BE.png)