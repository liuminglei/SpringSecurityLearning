package com.luas.securitylearning.config;

import com.luas.securitylearning.security.web.authentication.CustomSavedRequestAwareAuthenticationSuccessHandler;
import com.luas.securitylearning.service.EmailService;
import com.luas.securitylearning.service.SmsService;
import com.luas.securitylearning.service.WeChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private EmailService emailService;

    private SmsService smsService;

    private WeChatService wechatService;

    public SpringSecurityConfiguration(EmailService emailService, SmsService smsService, WeChatService wechatService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.wechatService = wechatService;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/plugins/**", "/images/**", "/fonts/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
        ;
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        CustomSavedRequestAwareAuthenticationSuccessHandler customSavedRequestAwareAuthenticationSuccessHandler = new CustomSavedRequestAwareAuthenticationSuccessHandler();
        customSavedRequestAwareAuthenticationSuccessHandler.setDefaultTargetUrl("/index");
        customSavedRequestAwareAuthenticationSuccessHandler.setEmailService(emailService);
        customSavedRequestAwareAuthenticationSuccessHandler.setSmsService(smsService);
        customSavedRequestAwareAuthenticationSuccessHandler.setWeChatService(wechatService);
        return customSavedRequestAwareAuthenticationSuccessHandler;
    }

}
