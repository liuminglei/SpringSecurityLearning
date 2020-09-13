package com.luas.securitylearning.security.web.access.intercept;

import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.servlet.ServletException;
import java.io.IOException;

public class CustomFilterSecurityInterceptor extends FilterSecurityInterceptor {

    private static final String FILTER_APPLIED = "__spring_security_customFilterSecurityInterceptor_filterApplied";

    @Override
    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        System.out.println(fi.getRequest().getAttribute(FILTER_APPLIED));
        System.out.println(isObserveOncePerRequest());

        if ((fi.getRequest() != null)
                && (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
                && isObserveOncePerRequest()) {
            // filter already applied to this request and user wants us to observe
            // once-per-request handling, so don't re-do security checking
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());

            logger.info("request " + fi.getRequestUrl() + " has already been security checking.");
        }
        else {
            logger.info("request " + fi.getRequestUrl() + " will be security checking.");

            // first time this request being called, so perform security checking
            if (fi.getRequest() != null && isObserveOncePerRequest()) {
                fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
            }

            InterceptorStatusToken token = super.beforeInvocation(fi);

            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            }
            finally {
                super.finallyInvocation(token);
            }

            super.afterInvocation(token, null);
        }
    }
}
