package com.luas.securitylearning.security.web.authentication;

import com.luas.securitylearning.service.EmailService;
import com.luas.securitylearning.service.SmsService;
import com.luas.securitylearning.service.WeChatService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String DEFAULT_FAILURE_URL = "/login_fail";

    private String defaultFailureUrl;

    private EmailService emailService;

    private SmsService smsService;

    private WeChatService weChatService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, exception);

        this.logger.info(String.format("IP %s 于 %s 尝试登录系统失败，失败原因：%s", request.getRemoteHost(), LocalDateTime.now(), exception.getMessage()));

        try {
            // 发邮件
            this.emailService.send();

            // 发短信
            this.smsService.send();

            // 发微信
            this.weChatService.send();
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
        }
    }


    public String getDefaultFailureUrl() {
        return defaultFailureUrl;
    }

    @Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        super.setDefaultFailureUrl(defaultFailureUrl);
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public SmsService getSmsService() {
        return smsService;
    }

    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    public WeChatService getWeChatService() {
        return weChatService;
    }

    public void setWeChatService(WeChatService weChatService) {
        this.weChatService = weChatService;
    }
}
