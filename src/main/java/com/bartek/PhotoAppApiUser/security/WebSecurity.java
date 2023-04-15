package com.bartek.PhotoAppApiUser.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private Environment environment;
    public WebSecurity(Environment environment) {
        this.environment = environment;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST,"/users/**")
                        .hasIpAddress(environment.getProperty("gateway.ip"))
                )
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/h2-console/**").permitAll()
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.headers().frameOptions().disable();

        return http.build();
    }
}
