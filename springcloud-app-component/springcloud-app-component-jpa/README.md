# JPA使用方法
- - -
## 1. 使用方法
* 1.1 启动类继承 ParentApplication
* 1.2 数据库映射实体类继承 AbstractAuditingEntity
* 1.3 加注解 @EnableJpaRepositories(repositoryBaseClass = BaseJPARepositoryImpl.class)
* 1.4 持久层接口继承 BaseJPARepository
* 1.5 需要审计功能，加上@EnableJpaAuditing(auditorAwareRef = "auditorAware")
* 1.6
```yaml
  jpa:
    show-sql: true
```