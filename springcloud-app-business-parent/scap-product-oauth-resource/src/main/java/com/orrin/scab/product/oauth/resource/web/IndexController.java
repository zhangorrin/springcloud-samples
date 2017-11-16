package com.orrin.scab.product.oauth.resource.web;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Api("IndexController相关api")
@RestController
public class IndexController {
    @RequestMapping(path = "api/index", method = RequestMethod.GET)
    String getMessages(Principal principal, HttpServletRequest request) {

        String url = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        return principal.getName();
    }

    @ApiOperation("获取application信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="header",name="username",dataType="String",required=true,value="用户的姓名",defaultValue="user"),
            @ApiImplicitParam(paramType="query",name="password",dataType="String",required=true,value="用户的密码",defaultValue="password")
    })
    @ApiResponses({
            @ApiResponse(code=400,message="请求参数没填好"),
            @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
    @RequestMapping(value="/application",method=RequestMethod.GET)
    public String getUser(@RequestHeader("username") String username, @RequestParam("password") String password) {
        return "username = " + username + " password = " + password;
    }
}
