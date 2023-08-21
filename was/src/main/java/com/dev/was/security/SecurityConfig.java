package com.dev.was.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final OAuth2MemberService oAuth2MemberService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/api/user/**").authenticated() // user 시작하는 uri는 로그인 필수
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // admin 시작하는 uri는 관리자 계정만 접근 가능
                .anyRequest().authenticated() //나머지 uri는 모든 접근 허용
                .and()
                .logout()
                .logoutUrl("/api/user/logout") // URL mapping for logout
                .invalidateHttpSession(true) // Invalidate HTTP session after logout
                .clearAuthentication(true) // Clear authentication information after logout
                .permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint()//로그인 완료 후 회원 정보 받기
                .userService(oAuth2MemberService)
                .and()
                .successHandler(loginSuccessHandler()) //OAuth 로그인이 성공하면 이동할 uri 설정
                .and().build();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    public static class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(
                HttpServletRequest request, HttpServletResponse response, Authentication auth) {
            try {
                HttpSession session = request.getSession();
                String sessionId = session.getId();
                String userId = auth.getName();
                // 이미 로그인 된 상황임.

                String url = request.getScheme() + "://" + request.getServerName();
                // dev 일 경우 proxy port 로 변경해서 redirect 한다.
                if (isDev)
                    response.sendRedirect(url + ":1133");
                else
                    response.sendRedirect(url + ":1132");


            } catch (Exception e) {
                // 여기까지 왔으면 이미 로그인은 성공된 경우임.
                logger.error("handle login success error.", e);
            }
        }
        @Value("${sppd.dev}")
        private boolean isDev;
    }

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
}