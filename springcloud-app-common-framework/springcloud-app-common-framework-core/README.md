# 使用方法
- - -
> 日志打印
* @MethodLog

> 统一异常
* BusinessException

> rest接口返回数据统一封装
* ResponseResult

> rest返回json过滤

* 在SpringMVC配置中加入JsonReturnHandler
	
```java
    @Configuration
    public class WebConfig extends WebMvcConfigurerAdapter {
        @Override
        public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
            returnValueHandlers.add(new JsonReturnHandler());
        }
    }
```

使用方法例如：
```java
    @RestLog
	@CheckCreditToken(isCheckCreditToken = true)
	@RequestMapping(value = "/business/profile/{type}", method = RequestMethod.GET)
	@JSON(type = UserMoreInfo.class, include = "realName,userId,identityCard")
	@JSON(type = UserBusinessIncludeMore.class,include = "platCode,userBusinessType,organizationId,platName,userBusinessStatus,userBusinessStatusDesc")
	@JSON(type = TSysAffix.class, include = "affixId,affixType,affixTypeDesc,affixUrl")
	public ResponseResult<UserAndBusinessAndLicenseInfo> getBusinessProfile(@PathVariable("type") String type, HttpServletRequest request){
	   return null;
	}

```