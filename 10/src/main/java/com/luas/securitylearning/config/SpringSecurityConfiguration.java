package com.luas.securitylearning.config;

import com.luas.securitylearning.security.web.authentication.CustomSimpleUrlAuthenticationFailureHandler;
import com.luas.securitylearning.service.EmailService;
import com.luas.securitylearning.service.SmsService;
import com.luas.securitylearning.service.WeChatService;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private EmailService emailService;

    private SmsService smsService;

    private WeChatService weChatService;

    public SpringSecurityConfiguration(EmailService emailService, SmsService smsService, WeChatService weChatService) {
        this.emailService = emailService;
        this.smsService = smsService;
        this.weChatService = weChatService;
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
                .defaultSuccessUrl("/index")
                .failureHandler(customSimpleUrlAuthenticationFailureHandler())
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/login_fail").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/logout_success")
                .permitAll()
                .and()
                .csrf().disable()
        ;
    }

    public AuthenticationFailureHandler customSimpleUrlAuthenticationFailureHandler() {
        CustomSimpleUrlAuthenticationFailureHandler customSimpleUrlAuthenticationFailureHandler = new CustomSimpleUrlAuthenticationFailureHandler();
        customSimpleUrlAuthenticationFailureHandler.setDefaultFailureUrl("/login_fail");
        customSimpleUrlAuthenticationFailureHandler.setEmailService(emailService);
        customSimpleUrlAuthenticationFailureHandler.setSmsService(smsService);
        customSimpleUrlAuthenticationFailureHandler.setWeChatService(weChatService);

        return customSimpleUrlAuthenticationFailureHandler;
    }

}
