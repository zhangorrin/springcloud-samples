package com.orrin.sca.framework.core.exception;

import com.orrin.sca.framework.core.model.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

public class GlobalWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseResult<Void> businessExceptionHandler(HttpServletRequest request, BusinessException be ) {
        logger.error(be.toString());
        ResponseResult<Void> responseResult = new ResponseResult<>();
        responseResult.setResponseCode(be.getErrorCode());
        responseResult.setResponseMsg(be.getMessage());
        return responseResult;
    }
}
