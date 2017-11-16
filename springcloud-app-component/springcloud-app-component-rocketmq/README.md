# 使用方法
- - -
## 1. 配置
在application.yml当中配置相关参数
```
spring:
  extend:
    rocketmq:
      namesrvAddr: 10.89.0.64:9876;10.89.0.65:9876 #服务器地址和端口，逗号隔开
      producer:
        instanceName: abcproduct  #生产者实例名称
        tranInstanceName: tran-abcproduct #带食物生产者实例名称
      consumer:
        instanceName: abcconsumer  #消费者实例名称
        subscribe:
          - subscribe: topic1:TagA || TagB || TagC  #消费者消费的主题和subkey
          - subscribe: topic2:*   #消费者消费的主题
```

## 2. 使用方法

[RocketMQ单机与集群安装教程](http://blog.csdn.net/jayjjb/article/details/69948357)

在ComponentScan扫描路径中加入com.orrin.sca.component.rocketmq