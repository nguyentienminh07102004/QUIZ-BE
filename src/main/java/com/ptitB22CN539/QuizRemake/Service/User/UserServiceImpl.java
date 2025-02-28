package com.ptitB22CN539.QuizRemake.Service.User;

import com.nimbusds.jwt.JWTClaimsSet;
import com.ptitB22CN539.QuizRemake.BeanApp.ConstantConfig;
import com.ptitB22CN539.QuizRemake.BeanApp.UserStatus;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserChangePasswordRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserLoginRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserRegisterRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSocialLogin;
import com.ptitB22CN539.QuizRemake.Domains.JwtEntity;
import com.ptitB22CN539.QuizRemake.Domains.UserEntity;
import com.ptitB22CN539.QuizRemake.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Jwt.JwtGenerator;
import com.ptitB22CN539.QuizRemake.Mapper.UserMapper;
import com.ptitB22CN539.QuizRemake.Repository.IJwtRepository;
import com.ptitB22CN539.QuizRemake.Repository.IUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserMapper userMapper;
    private final IUserRepository userRepository;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;
    private final IJwtRepository jwtRepository;

    @Value(value = "${maxLoginDevice}")
    private Integer maxLoginDevice;
    @Value(value = "${google.clientId}")
    private String clientId;
    @Value(value = "${google.clientSecret}")
    private String clientSecret;

    @Override
    @Transactional
    public UserEntity save(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new DataInvalidException(ExceptionVariable.EMAIL_EXISTS);
        }
        UserEntity user = userMapper.registerToEntity(userRegisterRequest);
        if (userRegisterRequest.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        } else {
            user.setStatus(userRegisterRequest.getStatus());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public JwtEntity login(UserLoginRequest userLogin) {
        try {
            UserEntity user = this.getUserByEmail(userLogin.getEmail());
            if ((userLogin.getIsSocial() == null || !userLogin.getIsSocial())
                    && !passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
                throw new DataInvalidException(ExceptionVariable.EMAIL_PASSWORD_NOT_CORRECT);
            }
            List<JwtEntity> jwtEntities = jwtRepository.findByUser_Email(user.getEmail());
            if (jwtEntities.size() >= maxLoginDevice) {
                String jit = null;
                for (JwtEntity jwtEntity : jwtEntities) {
                    JWTClaimsSet jwtClaimsSet = jwtGenerator.getSignedJWT(jwtEntity.getToken()).getJWTClaimsSet();
                    if (jwtClaimsSet.getExpirationTime().before(new Date(System.currentTimeMillis()))) {
                        jit = jwtClaimsSet.getJWTID();
                        break;
                    }
                }
                if (jit != null) {
                    // nếu có 1 token hết hạn
                    jwtRepository.deleteById(jit);
                } else {
                    throw new DataInvalidException(ExceptionVariable.ACCOUNT_LOGIN_MAX_DEVICE);
                }
            }
            JwtEntity jwt = jwtGenerator.generateJwtEntity(user);
            user.getListJwts().add(jwt);
            userRepository.save(user);
            return jwt;
        } catch (ParseException exception) {
            return null;
        }
    }

    @Override
    @Transactional
    public void changeStatus(List<String> ids) {
        ids.forEach(id -> {
            UserEntity user = this.getUserById(id);
            user.setStatus(user.getStatus().equals(UserStatus.INACTIVE) ? UserStatus.ACTIVE : UserStatus.INACTIVE);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.USER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request) {
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            JWTClaimsSet jwtClaimsSet = jwtGenerator.getSignedJWT(token.substring(7)).getJWTClaimsSet();
            jwtRepository.deleteById(jwtClaimsSet.getJWTID());
        } catch (ParseException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    @Transactional
    public void changePassword(UserChangePasswordRequest userChangePassword) {
        if (!userChangePassword.getConfirmPassword().equals(userChangePassword.getNewPassword())) {
            throw new DataInvalidException(ExceptionVariable.PASSWORD_CONFIRM_PASSWORD_NOT_MATCH);
        }
        UserEntity user = this.getMyInfo();
        if (passwordEncoder.matches(userChangePassword.getOldPassword(), user.getPassword())) {
            throw new DataInvalidException(ExceptionVariable.OLD_PASSWORD_NEW_PASSWORD_MATCH);
        }
        user.setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getMyInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.getUserByEmail(email);
    }

    @Override
    @Transactional
    @SuppressWarnings(value = "rawtypes")
    public JwtEntity loginSocial(UserSocialLogin userSocialLogin) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.googleapis.com/oauth2/v4/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
        MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add(OAuth2ParameterNames.CLIENT_ID, clientId);
        values.add(OAuth2ParameterNames.CLIENT_SECRET, clientSecret);
        values.add(OAuth2ParameterNames.CODE, userSocialLogin.getCode());
        values.add(OAuth2ParameterNames.REDIRECT_URI, "http://localhost:3000/login");
        values.add(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
        WebClient.RequestHeadersSpec<?> headersSpec = webClient.method(HttpMethod.POST)
                .body(BodyInserters.fromMultipartData(values));
        Mono<Map> response = headersSpec.exchangeToMono(res -> {
            if (res.statusCode().is2xxSuccessful()) {
                return res.bodyToMono(Map.class);
            } else if (res.statusCode().is4xxClientError()) {
                throw new DataInvalidException(ExceptionVariable.CODE_INVALID);
            } else {
                throw new DataInvalidException(ExceptionVariable.SERVER_ERROR);
            }
        });
        String accessToken = Objects.requireNonNull(response.block()).get(OAuth2ParameterNames.ACCESS_TOKEN).toString();
        webClient = WebClient.builder()
                .baseUrl("https://www.googleapis.com/oauth2/v3/userinfo")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();
        response = webClient.method(HttpMethod.GET).exchangeToMono(res -> {
            if (res.statusCode().is2xxSuccessful()) {
                return res.bodyToMono(Map.class);
            } else {
                throw new DataInvalidException(ExceptionVariable.SERVER_ERROR);
            }
        });
        String email = Objects.requireNonNull(response.block()).get("email").toString();
        if (userRepository.existsByEmail(email)) {
            return this.login(new UserLoginRequest(email, null, true));
        }
        UserEntity user = userMapper.registerToEntity(new UserRegisterRequest(null, email, ConstantConfig.DEFAULT_PASSWORD, ConstantConfig.DEFAULT_PASSWORD, null, UserStatus.ACTIVE));
        userRepository.save(user);
        return jwtGenerator.generateJwtEntity(user);
    }
}
