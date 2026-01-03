package com.ptitB22CN539.QuizRemake.Configuration.Security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.ptitB22CN539.QuizRemake.Common.Bean.ConstantConfiguration;
import com.ptitB22CN539.QuizRemake.Common.Enum.UserStatus;
import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Common.Jwt.JwtGenerator;
import com.ptitB22CN539.QuizRemake.Model.Entity.UserEntity;
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
public class WebSecurityConfiguration {
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
                                .requestMatchers(HttpMethod.POST, "/users/login/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.GET, "/users/count").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/change-status/{ids}").hasRole(ConstantConfiguration.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.POST, "/users/upload-avatar").authenticated()
                                .requestMatchers(HttpMethod.POST, "/users/forgot-password").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/forgot-password").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/logout").authenticated()
                                .requestMatchers(HttpMethod.POST, "/users/change-admin").hasRole(ConstantConfiguration.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.PUT, "/users/change-admin").hasRole(ConstantConfiguration.ROLE_ADMIN)
                                .requestMatchers(HttpMethod.GET, "/users/change-admin").permitAll()

                                .requestMatchers(HttpMethod.POST, "/chats").hasRole(ConstantConfiguration.ROLE_USER)
                                .requestMatchers(HttpMethod.POST, "/chats/files").hasRole(ConstantConfiguration.ROLE_USER)
                                .requestMatchers(HttpMethod.GET, "/chats/files/{id}").permitAll()

                                .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                                .requestMatchers(HttpMethod.POST, "/categories").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.PUT, "/categories").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.POST, "/categories/save-from-file").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.GET, "/categories/count").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.GET, "/categories/scroll").access(new AuthorizationNotRoleUser())

                                .requestMatchers(HttpMethod.GET, "/questions").permitAll()
                                .requestMatchers(HttpMethod.POST, "/questions").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.PUT, "/questions").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.DELETE, "/questions").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.GET, "/questions/count").access(new AuthorizationNotRoleUser())

                                .requestMatchers(HttpMethod.GET, "/tests").permitAll()
                                .requestMatchers(HttpMethod.GET, "/tests/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/tests").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.GET, "/tests/count").permitAll()

                                .requestMatchers(HttpMethod.POST, "/test-result/start").permitAll()
                                .requestMatchers(HttpMethod.POST, "/test-result/save-answer-test-result").hasRole(ConstantConfiguration.ROLE_USER)
                                .requestMatchers(HttpMethod.POST, "/test-result/finish").hasRole(ConstantConfiguration.ROLE_USER)
                                .requestMatchers(HttpMethod.GET, "/test-result/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/test-result/count").authenticated()
                                .requestMatchers(HttpMethod.GET, "/test-result/number-of-player-participating-test").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.GET, "/test-result/number-of-player-participating-test-for-time").access(new AuthorizationNotRoleUser())
                                .requestMatchers(HttpMethod.GET, "/test-result").hasRole(ConstantConfiguration.ROLE_ADMIN)

                                .requestMatchers(HttpMethod.GET, "/tests/same-category").permitAll()
                                .requestMatchers(HttpMethod.GET, "/test-result/test/{testResultId}").hasRole(ConstantConfiguration.ROLE_USER)
                                .requestMatchers(HttpMethod.GET, "/test-result/{testResultId}/question/{questionId}").hasRole(ConstantConfiguration.ROLE_USER)
                                .requestMatchers(HttpMethod.GET, "/tests/rating/user/{testId}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/tests/rate/{testId}/{rate}").hasAnyRole(ConstantConfiguration.ROLE_USER)

                                .requestMatchers("/ws**").permitAll()

                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                                .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> {
                    oauth2
                            .jwt(jwtConfigurer -> jwtConfigurer
                                    .decoder(jwtDecoder())
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
        authoritiesConverter.setAuthorityPrefix(ConstantConfiguration.ROLE_PREFIX);
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
