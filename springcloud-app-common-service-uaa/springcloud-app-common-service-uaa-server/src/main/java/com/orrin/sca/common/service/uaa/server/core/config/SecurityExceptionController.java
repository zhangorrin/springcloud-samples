package com.orrin.sca.common.service.uaa.server.core.config;


import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/securityException")
public class SecurityExceptionController {

    @RequestMapping(path = "/accessDenied", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult<Void> accessDenied() {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("20000");
        responseResult.setResponseMsg("not permission !");
        return responseResult;
    }

}
