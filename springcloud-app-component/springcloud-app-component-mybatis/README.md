# 工作流使用方法
- - -
## 1. 配置
在application.yml当中配置相关参数
```
mybatis:
    type-aliases-package: com.orrin.sca.business.product.model
    mapper-locations: classpath:mapper/*.xml

pagehelper:
    helperDialect: mysql
    reasonable: true
    supportMethodsArguments: true
    params: count=countSql
```