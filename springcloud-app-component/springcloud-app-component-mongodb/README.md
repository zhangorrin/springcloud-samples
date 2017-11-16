# 工作流使用方法
- - -
## 1. 配置
在application.yml当中配置相关参数
```
spring:
  data:
    mongodb:
      authentication-database:  # Authentication database name.
      database: test # Database name.
      field-naming-strategy:  # Fully qualified name of the FieldNamingStrategy to use.
      grid-fs-database:  # GridFS database name.
      host: localhost # Mongo server host.
      port: 27017 # Mongo server port.
      repositories:
        enabled: true # Enable Mongo repositories.
      #uri: mongodb://localhost/test
      username: # Mongo database URI. When set, host and port are ignored.
      password:  # Login password of the mongo server.
```

## 2. 使用方法

mongodb持久层接口继承 BaseMongoRepository