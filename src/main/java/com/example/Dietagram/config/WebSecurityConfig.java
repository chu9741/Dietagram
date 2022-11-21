package com.example.Dietagram.config;


import com.example.Dietagram.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@Component
public class WebSecurityConfig {

    @Autowired
    OAuthService oAuthService;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable()
        .headers().frameOptions().disable()
        .and()
        .logout().logoutSuccessUrl("/")
        .and()
        .oauth2Login()
        .defaultSuccessUrl("/oauth/loginInfo", true) // Feed창으로 이동?
        .userInfoEndpoint()
        .userService(oAuthService);

        return http.build();
    }
}
