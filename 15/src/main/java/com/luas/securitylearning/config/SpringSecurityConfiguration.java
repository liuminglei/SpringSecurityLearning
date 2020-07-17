package com.luas.securitylearning.config;

import com.luas.securitylearning.dao.FuncDao;
import com.luas.securitylearning.dao.RoleDao;
import com.luas.securitylearning.dao.UserDao;
import com.luas.securitylearning.domain.po.SysFuncRole;
import com.luas.securitylearning.security.core.userdetails.jdbc.CustomJdbcUserDetailsService;

import com.luas.securitylearning.security.web.access.intercept.CustomFilterSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private FuncDao funcDao;

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
                .failureUrl("/login_fail")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/logout_success")
                .permitAll()
                .and()
                .csrf().disable()
        ;

        http.addFilterAfter(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customJdbcUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    private UserDetailsService customJdbcUserDetailsService() {
        CustomJdbcUserDetailsService userDetailsService = new CustomJdbcUserDetailsService();
        userDetailsService.setUserDao(userDao);
        userDetailsService.setRoleDao(roleDao);
        return userDetailsService;
    }

    private FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        CustomFilterSecurityInterceptor filterSecurityInterceptor = new CustomFilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(new DefaultFilterInvocationSecurityMetadataSource(obtainRequestMap()));
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
        return filterSecurityInterceptor;
    }

    private AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();
        voters.add(new RoleVoter());

        return new AffirmativeBased(voters);
    }

    /**
     * <p>
     * 本例我们使用功能地址为 <code>/x/y/z</code> 格式（注意，z可能不存在）。<br>
     * <code>x</code> 代表模块或功能，<code>y</code> 代表功能或操作，<code>z</code> 代表操作或无；<br>
     * 功能一旦被分配给角色，即代表拥有该角色的用户即拥有了该功能的任何操作权限。<br>
     * 因此，我们使用 <code>/x/y/**</code> ant风格来进行匹配。<br>
     * 另外，<strong>假设某一功能需要N个角色控制，我们默认拥有任一角色即可对此功能进行访问</strong>。
     * </p>
     *
     * <p>
     * 在此方法中，查询系统所有已分派角色的功能，转换其url为ant风格，且组织其需要的权限列表。
     * </p>
     * <p>
     * 另外，需要注意，此处我们使用的角色的code字段，而不是其id，如使用其id，则需要修改 {@link CustomJdbcUserDetailsService#loadUserByUsername(String)}
     * 中关于设置role的逻辑，需要查询当前用户所拥有的角色的id，而不是其code。目前，正是查询的当前用户
     * 所拥有的角色的code。所以，此处也需要使用code，保持一致。
     * </p>
     * <p>
     * 设置ConfigAttribute为 ROLE_ 前缀加上角色标识，与 {@link CustomJdbcUserDetailsService#loadUserByUsername(String)} 中
     * 组织 {@link org.springframework.security.core.userdetails.User.UserBuilder#roles(String...)} 设置角色标识呼应
     * </p>
     * @return
     */
    private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> obtainRequestMap() {
        List<SysFuncRole> sysFuncRoles = this.funcDao.listFuncRole();

        if (CollectionUtils.isEmpty(sysFuncRoles)) {
            return new LinkedHashMap<>();
        }

        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

        for (SysFuncRole sysFuncRole : sysFuncRoles) {
            Collection<ConfigAttribute> configAttributes = requestMap.get(sysFuncRole.getFuncId());

            if (configAttributes == null) {
                configAttributes = new HashSet<>();
            }

            // 注意此处，我们设置ConfigAttribute为 ROLE_ 前缀加上角色标识，与 CustomJdbcUserDetailsService 里面组织UserDetails设置角色标识呼应
            configAttributes.add(new SecurityConfig("ROLE_" + sysFuncRole.getRoleCode()));
            requestMap.put(new AntPathRequestMatcher(determineAntUrl(sysFuncRole.getUrl())), configAttributes);
        }


        return requestMap;
    }

    /**
     * 去掉最后一个 <code>/</code> 后的内容，以 <code>**</code> 代替，以匹配 <code>ant</code> 风格。
     * @param url 功能地址
     * @return ant风格地址
     */
    private String determineAntUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        return url.substring(0, url.lastIndexOf("/")) + "/**";
    }

}
