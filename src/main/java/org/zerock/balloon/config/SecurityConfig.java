package org.zerock.balloon.config;
//모든 시큐리티 관련 설정들이 추가 되는 곳

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.zerock.balloon.security.handler.BalloonAuthFailureHandler;
import org.zerock.balloon.security.handler.BalloonLoginSuccessHandler;
import org.zerock.balloon.security.service.BalloonUserDetailsService;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //@PreAuthorize 사용하려고 작성
@RequiredArgsConstructor
public class SecurityConfig {

    private final BalloonUserDetailsService userDetailsService;

//    private final AuthenticationProvider authenticationProvider;

    /* AuthenticationManager Bean 등록 */
//    @Bean
//    public AuthenticationManager authenticationManager() {
//        List<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
//        authenticationProviderList.add(authenticationProvider);
//        ProviderManager authenticationManager = new ProviderManager();
//        authenticationManager.setAuthenticationEventPublisher(defaultAuthenticationEventPublisher());
//        return authenticationManager;
//    }
//
//    @Bean
//    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
//        return new DefaultAuthenticationEventPublisher();
//    }




    //패스워드 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //시큐리티 관련 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{


        //form 로그인 처리
        httpSecurity.formLogin()
                .loginPage("/balloon/login") //디자인 로그인 페이지로
                .loginProcessingUrl("/loginProcess") // 로그인 처리
                .failureHandler(failureHandler()) //로그인 실패시 메시지 출력 핸들러
                .defaultSuccessUrl("/balloon/main"); // 로그인 성공 후 이동할 페이지
//                .failureUrl("/balloon/auth/login"); //로그인 실패 시 이동 페이지 - failureHandler쓰려고 비활성화

        //CSRF토큰 발행 코드 - 일단 비활성화
        httpSecurity.csrf().disable();

        //로그아웃 처리. CSRF토큰 사용시 post방식, 미사용시 get방식
        httpSecurity.logout()
                .logoutUrl("/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
                .logoutSuccessUrl("/balloon/main") // 로그아웃 성공 후 이동페이지
                .deleteCookies("JSESSIONID", "remember-me"); // 로그아웃 후 쿠키 삭제

        /*
        .addLogoutHandler( ...생략... ) // 로그아웃 핸들러
        .logoutSuccessHandler( ...생략... ) // 로그아웃 성공 후 핸들러
        */
        // 인증된 사용자이지만 인가되지 않은 경로에 접근시 리다이랙팅 시킬 uri 지정
        //http.exceptionHandling().accessDeniedPage("/forbidden");

        //소셜 로그인
        httpSecurity.oauth2Login().successHandler(successHandler());

        //뭐냥... Not injecting HSTS header since it did not match request to [Is Secure] 이 오류 때문에 추가
//        httpSecurity.headers()
//                .httpStrictTransportSecurity()
//                .maxAgeInSeconds(31536000)
//                .includeSubDomains(true)
//                .preload(true);

        //자동로그인(remember me)
        httpSecurity.rememberMe()
                .tokenValiditySeconds(60*60*24*7) //초단위. 7일 유지
                .userDetailsService(userDetailsService);

        return httpSecurity.build();
    }

    //successHandler
    @Bean
    public BalloonLoginSuccessHandler successHandler(){
        return new BalloonLoginSuccessHandler();
    }

    //loginFailureHandler
    @Bean
    public BalloonAuthFailureHandler failureHandler(){
        return new BalloonAuthFailureHandler();
    }

}