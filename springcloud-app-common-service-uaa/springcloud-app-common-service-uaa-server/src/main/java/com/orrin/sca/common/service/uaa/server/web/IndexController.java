package com.orrin.sca.common.service.uaa.server.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class IndexController {

    @Value("${sys_env}")
    private String sysEvn;

    @GetMapping(path = "/sysenv")
    public String sysEvn() {
        return this.sysEvn;
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test() {
        return "test";
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    String getMessages(Principal principal, HttpServletRequest request) {

        String url = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        return principal.getName();
    }
}
