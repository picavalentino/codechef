package com.codechef.codechef.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth.requestMatchers("/mypage").authenticated()
                .requestMatchers("/css/**" , "/js/**").permitAll()
                .anyRequest().permitAll());

        http.formLogin((auth) -> auth.loginPage("/login").loginProcessingUrl("/loginMember").usernameParameter("email").defaultSuccessUrl("/main", true).successHandler(new CustomLoginSuccessHandler()).permitAll());
        http.csrf((auth) -> auth.disable()); // csrf -> 악의적인 기계적 공격에서 방어하기 위해 사용(여기서는 사용하지 않겠다고 선언)

        http.logout((auth) -> auth.logoutUrl("/logout").logoutSuccessUrl("/main").permitAll());

        http.sessionManagement((auth) -> auth.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                // 로그인과 같은 처리로 세션이 활성화되지 않으면 타임아웃이 발생하지 않음
                .invalidSessionUrl("/main")
                // 유효하지 않은 세션 -> 다 로그인 화면으로
                .sessionFixation().none()
                // 세션이 고정되지 않도록 설정
                .maximumSessions(1).expiredUrl("/login"));

        return http.build();
    }



}
