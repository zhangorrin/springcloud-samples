# 使用方法
- - -
> json处理
* json序列化与反序列化 JacksonUtils
* json过滤 CustomerJsonSerializer

> Security
* 获取当前登录人

> Spring 工具
* 获取Bean SpringUtil

> 日期处理
* InstantFormat

> 线程池跟踪封装
```java
ExecutorService executor = new TraceThreadPoolExecutor(corePoolSize, maximumPoolSize,
                                                        keepAliveTime, TimeUnit.SECONDS,
                                                        new LinkedBlockingQueue<Runnable>());
```

> 参数校验
* ParameterValidatorUtils：使用hibernate validator校验java bean