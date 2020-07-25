package com.luas.securitylearning.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @InitBinder
    public void initBinder(WebDataBinder binder) {

    }

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        model.addAttribute("request", request);
        model.addAttribute("base", request.getContextPath());
        model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd E")));
    }

    @ExceptionHandler(value = { Exception.class })
    public Object handleException(HttpServletRequest request, Exception exception) {
        logger.error(exception.getMessage(), exception);

        Map<String, Object> result = new HashMap<>();
        result.put("message", exception.getMessage());
        result.put("requestUrl", request.getServletPath().substring(request.getContextPath().length()));


        return result;
    }

}
