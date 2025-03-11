package com.ptitB22CN539.QuizRemake.Security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.ptitB22CN539.QuizRemake.BeanApp.ConstantConfig;
import com.ptitB22CN539.QuizRemake.BeanApp.UserStatus;
import com.ptitB22CN539.QuizRemake.Domains.UserEntity;
import com.ptitB22CN539.QuizRemake.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Jwt.JwtGenerator;
import com.ptitB22CN539.QuizRemake.Service.Jwt.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value(value = "${api}")
    private String API_PREFIX;
    @Value(value = "${signerKey}")
    private String signerKey;
    private final AccessDeniedHandler accessDeniedHandler;
    private final IJwtService jwtService;
    private final JwtGenerator jwtGenerator;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(HttpMethod.POST, "/%s/users/login/**".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.POST, "/%s/users/register".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.GET, "/%s/users/".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.GET, "/%s/users/count".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.PUT, "/%s/users/change-status/{ids}".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.POST, "/%s/users/upload-avatar".formatted(API_PREFIX)).permitAll()

                                .requestMatchers(HttpMethod.GET, "/%s/categories/".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.POST, "/%s/categories/".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.GET, "/%s/categories/count".formatted(API_PREFIX)).permitAll()

                                .requestMatchers(HttpMethod.GET, "/%s/questions/".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.POST, "/%s/questions/".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.GET, "/%s/questions/count".formatted(API_PREFIX)).permitAll()

                                .requestMatchers(HttpMethod.GET, "/%s/tests/".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.GET, "/%s/tests/{id}".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.POST, "/%s/tests/".formatted(API_PREFIX)).hasRole(ConstantConfig.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.GET, "/%s/tests/count".formatted(API_PREFIX)).permitAll()

                                .requestMatchers(HttpMethod.POST, "/%s/test-result/start".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.POST, "/%s/test-result/finish".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.GET, "/%s/test-result/{id}".formatted(API_PREFIX)).permitAll()
                                .requestMatchers(HttpMethod.GET, "/%s/test-result/count".formatted(API_PREFIX)).permitAll()

                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                                .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> {
                    oauth2
                            .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                                    .jwtAuthenticationConverter(jwtAuthenticationConverter()));
                    oauth2.authenticationEntryPoint(authenticationEntryPoint);
                });
                httpSecurity.cors(cors -> corsFilter());
                httpSecurity.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler));
        return httpSecurity.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource url = new UrlBasedCorsConfigurationSource();
        url.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(url);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix(ConstantConfig.ROLE_PREFIX);
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                JWTClaimsSet claimsSet = jwtGenerator.getSignedJWT(token).getJWTClaimsSet();
                if (claimsSet.getJWTID() == null || !jwtService.existsById(claimsSet.getJWTID())) {
                    throw new DataInvalidException(ExceptionVariable.TOKEN_INVALID);
                }
                UserEntity user = jwtService.findById(claimsSet.getJWTID()).getUser();
                if (!user.getEmail().equals(claimsSet.getSubject())) {
                    throw new DataInvalidException(ExceptionVariable.TOKEN_INVALID);
                }
                if (user.getStatus().equals(UserStatus.INACTIVE)) {
                    throw new DataInvalidException(ExceptionVariable.USER_LOCKED);
                }
                SecretKeySpec spec = new SecretKeySpec(signerKey.getBytes(), MacAlgorithm.HS512.getName());
                return NimbusJwtDecoder.withSecretKey(spec)
                        .macAlgorithm(MacAlgorithm.HS512)
                        .build()
                        .decode(token);
            } catch (ParseException e) {
                throw new DataInvalidException(ExceptionVariable.TOKEN_INVALID);
            }
        };
    }
}
