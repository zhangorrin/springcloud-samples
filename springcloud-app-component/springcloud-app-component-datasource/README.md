# 工作流使用方法
- - -
## 1. 配置
在application.yml当中配置相关参数
```yaml
#JDBC 配置
#更多配置参考 https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter
spring:
  datasource:
    druid:
      url: #jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true
      username:
      password:
      driver-class-name: com.mysql.jdbc.Driver
      filter:
        stat:
          db-type: mysql
          log-slow-sql: true
          slow-sql-millis: 2000
```

```yaml
# 输出可执行的SQL by log4jdbc
spring:
  datasource:
    druid:
      url: #jdbc:log4jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true
      username:
      password:
      driver-class-name: net.sf.log4jdbc.DriverSpy
      filter:
        stat:
          db-type: mysql
          log-slow-sql: true
          slow-sql-millis: 2000

```