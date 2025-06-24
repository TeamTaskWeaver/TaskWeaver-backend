package taskweaver.taskweaver_backend.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import taskweaver.taskweaver_backend.auth.CustomAuthenticationEntryPoint;
import taskweaver.taskweaver_backend.auth.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final String[] allowedUrls = {
            "/", "/swagger-ui/**", "/v3/**",
            "/v1/auth/sign-in",
            "/v1/auth/refresh",
            "/v1/auth/kakao" // 카카오 콜백 경로는 인증 없이 접근 허용
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                // [핵심] 기본 제공되는 모든 로그인 폼과 인증 방식을 비활성화
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .oauth2Login(oauth -> oauth.disable()) // OAuth2 자동 로그인 처리 비활성화

                // 세션은 사용하지 않으므로 STATELESS로 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청 경로별 권한 설정
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(allowedUrls).permitAll()
                                .anyRequest().authenticated()
                )
                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

