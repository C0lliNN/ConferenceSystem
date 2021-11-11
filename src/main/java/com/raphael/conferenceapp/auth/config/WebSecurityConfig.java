package com.raphael.conferenceapp.auth.config;

import com.raphael.conferenceapp.auth.web.filter.FilterChainExceptionHandler;
import com.raphael.conferenceapp.auth.web.filter.JWTAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@EnableWebSecurity
@AllArgsConstructor
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JWTAuthorizationFilter authorizationFilter;
    private final FilterChainExceptionHandler filterChainExceptionHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class)
                .addFilterAfter(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/v3/api-docs/**", "/docs/**", "/swagger-ui/**", "/auth/**").permitAll()
                .anyRequest().authenticated();
    }
}
