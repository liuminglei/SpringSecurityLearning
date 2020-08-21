package com.luas.securitylearning.security.access.vote;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Votes to grant access if all the <code>GrantedAuthority</code> matching to the all
 * <code>ConfigAttribute</code> starting with the role prefix.
 */
public class AllMatchRoleVoter extends RoleVoter {

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        if (authentication == null) {
            return ACCESS_DENIED;
        }

        int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);

        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;

                // Attempt to find all matching granted authority
                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        result = ACCESS_GRANTED;
                        break;
                    }
                }

                if (result == ACCESS_DENIED) {
                    return ACCESS_DENIED;
                }
            }
        }

        return result;
    }

    Collection<? extends GrantedAuthority> extractAuthorities(
            Authentication authentication) {
        return authentication.getAuthorities();
    }
}
