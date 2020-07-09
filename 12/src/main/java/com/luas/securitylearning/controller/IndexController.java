package com.luas.securitylearning.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

}
