package com.luas.securitylearning.security.access.vote;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.List;

/**
 * 类似于 {@link org.springframework.security.access.vote.ConsensusBased}，
 * 在其基础上，增加了必须超过半数（等于半数不可以）的条件。
 */
public class MoreThanHalfBased extends AbstractAccessDecisionManager {

    private boolean allowIfEqualGrantedDeniedDecisions = false;

    private transient int voters;

    public MoreThanHalfBased(List<AccessDecisionVoter<?>> decisionVoters) {
        super(decisionVoters);
        voters = decisionVoters.size();
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        int grant = 0;
        int deny = 0;

        for (AccessDecisionVoter voter : getDecisionVoters()) {
            int result = voter.vote(authentication, object, configAttributes);

            if (logger.isDebugEnabled()) {
                logger.debug("Voter: " + voter + ", returned: " + result);
            }

            switch (result) {
                case AccessDecisionVoter.ACCESS_GRANTED:
                    grant++;

                    break;

                case AccessDecisionVoter.ACCESS_DENIED:
                    deny++;

                    break;

                default:
                    break;
            }
        }

        boolean moreThanHalf = moreThanHalf(grant);

        if (grant > deny) {
            if (moreThanHalf) {
                return;
            } else {
                throw new AccessDeniedException(messages.getMessage(
                        "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
            }
        }

        if (deny > grant) {
            throw new AccessDeniedException(messages.getMessage(
                    "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
        }

        if ((grant == deny) && (grant != 0)) {
            if (this.allowIfEqualGrantedDeniedDecisions && moreThanHalf) {
                return;
            }
            else {
                throw new AccessDeniedException(messages.getMessage(
                        "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
            }
        }

        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions();
    }

    private boolean moreThanHalf(int grant) {
        return grant > Math.floorDiv(voters, 2);
    }

}
