package com.luas.securitylearning.security.web.authentication.rememberme;

import com.luas.securitylearning.service.EmailService;
import com.luas.securitylearning.service.SmsService;
import com.luas.securitylearning.service.WeChatService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class CustomRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {

    private EmailService emailService;

    private SmsService smsService;

    private WeChatService weChatService;

    public CustomRememberMeAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.onSuccessfulAuthentication(request, response, authentication);

        this.logger.info(String.format("IP %s，用户 %s， 于 %s 退出系统。", request.getRemoteHost(), authentication.getName(), LocalDateTime.now()));

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

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        super.onUnsuccessfulAuthentication(request, response, exception);

        this.logger.info(String.format("IP %s，于 %s 记住我登录失败。", request.getRemoteHost(), exception.getMessage(), LocalDateTime.now()));

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
