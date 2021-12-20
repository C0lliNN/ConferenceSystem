package com.raphael.conferenceapp.conferencemanagement.web;

import com.raphael.conferenceapp.auth.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser user) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User principal = User.builder().id(user.id()).build();

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, null);
        context.setAuthentication(auth);

        return context;
    }
}
