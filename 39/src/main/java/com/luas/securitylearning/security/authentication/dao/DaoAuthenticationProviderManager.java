package com.luas.securitylearning.security.authentication.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;

import java.util.List;

public class DaoAuthenticationProviderManager implements AuthenticationManager, MessageSourceAware,
        InitializingBean {

    private static final Log logger = LogFactory.getLog(DaoAuthenticationProviderManager.class);

    private AuthenticationEventPublisher eventPublisher = new DaoAuthenticationProviderManager.NullEventPublisher();
    private DaoAuthenticationProvider provider;
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private AuthenticationManager parent;
    private boolean eraseCredentialsAfterAuthentication = true;

    public DaoAuthenticationProviderManager(DaoAuthenticationProvider provider) {
        this(provider, null);
    }

    public DaoAuthenticationProviderManager(DaoAuthenticationProvider provider,
                           AuthenticationManager parent) {
        Assert.notNull(provider, "dao provider cannot be null");
        this.provider = provider;
        this.parent = parent;
        checkState();
    }

    // ~ Methods
    // ========================================================================================================

    public void afterPropertiesSet() throws Exception {
        checkState();
    }

    private void checkState() {
        if (parent == null && provider == null) {
            throw new IllegalArgumentException(
                    "A parent AuthenticationManager or a DaoAuthenticationProvider is required");
        }
    }

    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        Class<? extends Authentication> toTest = authentication.getClass();
        AuthenticationException lastException = null;
        AuthenticationException parentException = null;
        Authentication result = null;
        Authentication parentResult = null;
        boolean debug = logger.isDebugEnabled();

        if (provider.supports(toTest)) {
            if (debug) {
                logger.debug("Authentication attempt using "
                        + provider.getClass().getName());
            }

            try {
                result = provider.authenticate(authentication);
            }
            catch (AccountStatusException e) {
                prepareException(e, authentication);
                // SEC-546: Avoid polling additional providers if auth failure is due to
                // invalid account status
                throw e;
            }
            catch (InternalAuthenticationServiceException e) {
                prepareException(e, authentication);
                throw e;
            }
            catch (AuthenticationException e) {
                lastException = e;
            }

            if (result != null) {
                copyDetails(authentication, result);

                if (result == null && parent != null) {
                    // Allow the parent to try.
                    try {
                        result = parentResult = parent.authenticate(authentication);
                    }
                    catch (ProviderNotFoundException e) {
                        // ignore as we will throw below if no other exception occurred prior to
                        // calling parent and the parent
                        // may throw ProviderNotFound even though a provider in the child already
                        // handled the request
                    }
                    catch (AuthenticationException e) {
                        lastException = parentException = e;
                    }
                }

                if (result != null) {
                    if (eraseCredentialsAfterAuthentication
                            && (result instanceof CredentialsContainer)) {
                        // Authentication is complete. Remove credentials and other secret data
                        // from authentication
                        ((CredentialsContainer) result).eraseCredentials();
                    }

                    // If the parent AuthenticationManager was attempted and successful than it will publish an AuthenticationSuccessEvent
                    // This check prevents a duplicate AuthenticationSuccessEvent if the parent AuthenticationManager already published it
                    if (parentResult == null) {
                        eventPublisher.publishAuthenticationSuccess(result);
                    }
                    return result;
                }
            }

        } else {
            lastException = new ProviderNotFoundException(messages.getMessage(
                    "ProviderManager.providerNotFound",
                    new Object[] { toTest.getName() },
                    "No AuthenticationProvider found for {0}"));
        }


        // Parent was null, or didn't authenticate (or throw an exception).

        if (lastException == null) {
            lastException = new ProviderNotFoundException(messages.getMessage(
                    "ProviderManager.providerNotFound",
                    new Object[] { toTest.getName() },
                    "No AuthenticationProvider found for {0}"));
        }

        // If the parent AuthenticationManager was attempted and failed than it will publish an AbstractAuthenticationFailureEvent
        // This check prevents a duplicate AbstractAuthenticationFailureEvent if the parent AuthenticationManager already published it
        if (parentException == null) {
            prepareException(lastException, authentication);
        }

        throw lastException;
    }

    @SuppressWarnings("deprecation")
    private void prepareException(AuthenticationException ex, Authentication auth) {
        eventPublisher.publishAuthenticationFailure(ex, auth);
    }

    /**
     * Copies the authentication details from a source Authentication object to a
     * destination one, provided the latter does not already have one set.
     *
     * @param source source authentication
     * @param dest the destination authentication object
     */
    private void copyDetails(Authentication source, Authentication dest) {
        if ((dest instanceof AbstractAuthenticationToken) && (dest.getDetails() == null)) {
            AbstractAuthenticationToken token = (AbstractAuthenticationToken) dest;

            token.setDetails(source.getDetails());
        }
    }

    public DaoAuthenticationProvider getProvider() {
        return provider;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setAuthenticationEventPublisher(
            AuthenticationEventPublisher eventPublisher) {
        Assert.notNull(eventPublisher, "AuthenticationEventPublisher cannot be null");
        this.eventPublisher = eventPublisher;
    }

    /**
     * If set to, a resulting {@code Authentication} which implements the
     * {@code CredentialsContainer} interface will have its
     * {@link CredentialsContainer#eraseCredentials() eraseCredentials} method called
     * before it is returned from the {@code authenticate()} method.
     *
     * @param eraseSecretData set to {@literal false} to retain the credentials data in
     * memory. Defaults to {@literal true}.
     */
    public void setEraseCredentialsAfterAuthentication(boolean eraseSecretData) {
        this.eraseCredentialsAfterAuthentication = eraseSecretData;
    }

    public boolean isEraseCredentialsAfterAuthentication() {
        return eraseCredentialsAfterAuthentication;
    }

    private static final class NullEventPublisher implements AuthenticationEventPublisher {
        public void publishAuthenticationFailure(AuthenticationException exception,
                                                 Authentication authentication) {
        }

        public void publishAuthenticationSuccess(Authentication authentication) {
        }
    }

}
