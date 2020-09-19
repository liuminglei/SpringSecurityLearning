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

        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(60, 40, 4, 25);
        try {
            ServletOutputStream outputStream = response.getOutputStream();

            //图形验证码写出：可以写出到流，也可以写出到文件如captchaa.write("d:/circle_captcha.jpeg");
            captcha.write(outputStream);

            //从带有圆圈类型的图形验证码图片中获取它的字符串验证码(获取字符串验证码要在图形验证码写出wirte后面才行，不然得到的值为null)
            String code = captcha.getCode();

            //将字符串验证码保存到session中
            session.setAttribute("captcha", code);

            logger.info("session id {}， 生成的验证码 {}", session.getId(), code);

            //关闭流
            outputStream.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
