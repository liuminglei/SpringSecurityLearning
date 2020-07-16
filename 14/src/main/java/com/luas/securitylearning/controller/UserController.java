package com.luas.securitylearning.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @PreAuthorize("hasRole('User')")
    @RequestMapping("/index")
    public String index() {
        return "/user/index";
    }


}
