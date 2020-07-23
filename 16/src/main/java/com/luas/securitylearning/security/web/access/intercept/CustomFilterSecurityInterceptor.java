package com.luas.securitylearning.security.web.access.intercept;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.*;
import java.io.IOException;

public class CustomFilterSecurityInterceptor extends FilterSecurityInterceptor {

    // ~ Static fields/initializers
    // =====================================================================================

    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";

    // ~ Instance fields
    // ================================================================================================

    private FilterInvocationSecurityMetadataSource securityMetadataSource;
    private boolean observeOncePerRequest = true;

    // ~ Methods
    // ========================================================================================================

    /**
     * Not used (we rely on IoC container lifecycle services instead)
     *
     * @param arg0 ignored
     *
     * @throws ServletException never thrown
     */
    public void init(FilterConfig arg0) throws ServletException {
    }

    /**
     * Not used (we rely on IoC container lifecycle services instead)
     */
    public void destroy() {
    }

    /**
     * Method that is actually called by the filter chain. Simply delegates to the
     * {@link #invoke(FilterInvocation)} method.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     *
     * @throws IOException if the filter chain fails
     * @throws ServletException if the filter chain fails
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource newSource) {
        this.securityMetadataSource = newSource;
    }

    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        // 请求重复安全检查验证
        if ((fi.getRequest() != null)
                && (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
                && observeOncePerRequest) {

            // 请求已经进行过安全检查，直接放过
            // filter already applied to this request and user wants us to observe
            // once-per-request handling, so don't re-do security checking
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }
        else {
            // 请求第一次进行安全检查，放入FILTER_APPLIED属性值
            // first time this request being called, so perform security checking
            if (fi.getRequest() != null && observeOncePerRequest) {
                fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
            }

            // 1. 从 SecurityMetadataSource 中获取 request 对应的权限
            // 2. authenticationManager 身份认证
            // 3. accessDecisionManager 权限校验
            // 4. runAsManager 另一身份运行
            // 5. SecurityContext 清理
            // 6. afterInvocationManager 后置权限校验
            InterceptorStatusToken token = super.beforeInvocation(fi);

            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            }
            finally {
                // SecurityContext 清理，回退回原用户身份
                super.finallyInvocation(token);
            }

            // 后置权限校验，SecurityContext 清理，回退回原用户身份
            super.afterInvocation(token, null);
        }
    }

    /**
     * Indicates whether once-per-request handling will be observed. By default this is
     * <code>true</code>, meaning the <code>FilterSecurityInterceptor</code> will only
     * execute once-per-request. Sometimes users may wish it to execute more than once per
     * request, such as when JSP forwards are being used and filter security is desired on
     * each included fragment of the HTTP request.
     *
     * @return <code>true</code> (the default) if once-per-request is honoured, otherwise
     * <code>false</code> if <code>FilterSecurityInterceptor</code> will enforce
     * authorizations for each and every fragment of the HTTP request.
     */
    public boolean isObserveOncePerRequest() {
        return observeOncePerRequest;
    }

    public void setObserveOncePerRequest(boolean observeOncePerRequest) {
        this.observeOncePerRequest = observeOncePerRequest;
    }


}
