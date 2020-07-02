package com.luas.securitylearning.controller;

import cn.hutool.core.convert.Convert;
import com.luas.securitylearning.security.web.LoginError;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String typeStr = request.getParameter("type");

        Integer type = StringUtils.hasLength(typeStr) ? Convert.toInt(typeStr) : null;

        model.addAttribute("message", LoginError.getMessage(type));

        return "login";
    }

    @RequestMapping("/login_fail")
    public String loginFail(HttpServletRequest request, Model model) {
        LoginError loginError = determineErrorType(request);

        model.addAttribute("errorMessage", loginError != null ? loginError.getMessage() : null);

        return "login_fail";
    }

    private LoginError determineErrorType(HttpServletRequest request) {
        String typeStr = request.getParameter("error");

        return typeStr == null ? null : LoginError.resolve(Integer.valueOf(typeStr));
    }

}
