package com.coviscon.memberservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final OAuthMemberService OAuthMemberService;
    /* 로그인 실패 핸들러 의존성 주입 */
    private final AuthenticationFailureHandler customFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .and()

                .formLogin()
                .loginPage("http://3.39.96.34:8000/member-service/auth/login")
                .failureHandler(customFailureHandler) /* 로그인 실패 핸들러 */
                .and()
                .logout()
                .logoutUrl("http://3.39.96.34:8000/member-service/auth/logout")
                .logoutSuccessUrl("http://3.39.96.34:8000/member-service/")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()

                .oauth2Login()
                .loginPage("/loginForm")
                .failureHandler(customFailureHandler) /* 로그인 실패 핸들러 */
                .defaultSuccessUrl("http://3.39.96.34:8000/member-service/") //OAuth 구글 로그인이 성공하면 이동할 uri 설정
                .userInfoEndpoint() // 로그인 완료 후 회원 정보 받기
                .userService(OAuthMemberService)
                .and()
                .and()
                .logout()
                .logoutUrl("http://3.39.96.34:8000/member-service/auth/logout")
                .logoutSuccessUrl("http://3.39.96.34:8000/member-service/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .build();
    }
}
