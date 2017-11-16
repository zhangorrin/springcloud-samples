# 工作流使用方法
- - -
## 1. 配置
在application.yml当中配置相关参数
```
spring:
  activiti:
    separateDB: true ##true使用单独数据库，false使用springcloud-app-component-datasource中配置的数据库
    database:  ##当separateDB=true时生效
      url: #jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true
      username:
      password:
      driver-class-name: com.mysql.jdbc.Driver
```

## 2. 使用方法

在ComponentScan扫描路径中加入com.orrin.sca.component.activiti