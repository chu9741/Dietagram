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
        .defaultSuccessUrl("http://localhost:3000/login/oauth2/code/kakao") // Feed창으로 이동?
        .userInfoEndpoint()
        .userService(oAuthService);

        return http.build();
    }
}
