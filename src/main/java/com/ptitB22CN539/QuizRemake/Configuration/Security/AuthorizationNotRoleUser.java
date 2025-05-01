package com.ptitB22CN539.QuizRemake.Configuration.Security;

import com.ptitB22CN539.QuizRemake.Common.Bean.ConstantConfiguration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class AuthorizationNotRoleUser implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        return new AuthorizationDecision(!authentication.get()
                .getAuthorities()
                .iterator().next()
                .getAuthority()
                .equals(ConstantConfiguration.ROLE_USER));
    }
}
