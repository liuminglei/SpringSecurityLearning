package com.luas.securitylearning.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class CaptchaController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/captcha/generate")
    public void captchaGenerate(HttpSession session, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(80, 40, 4, 25);
        try {
            ServletOutputStream outputStream = response.getOutputStream();

            // 图形验证码写出既输出到流，当然也可以输出到文件，如captcha.write("d:/circle_captcha.jpeg");
            captcha.write(outputStream);

            // 从带有圆圈类型的图形验证码图片中获取其中的字符串验证码
            // 注意，获取字符串验证码要在图形验证码write后，不然得到的值为null
            String captchaCode = captcha.getCode();

            // 将字符串验证码保存到session中
            session.setAttribute("captcha", captchaCode);

            logger.info("session id {}， 生成的验证码 {}", session.getId(), captchaCode);

            //关闭流
            outputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
