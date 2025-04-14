package com.ptitB22CN539.QuizRemake.Service.User;

import com.nimbusds.jwt.JWTClaimsSet;
import com.ptitB22CN539.QuizRemake.Common.BeanApp.ConstantConfig;
import com.ptitB22CN539.QuizRemake.Common.BeanApp.UserStatus;
import com.ptitB22CN539.QuizRemake.DTO.DTO.JwtDTO;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserChangePasswordRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserLoginRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserRegisterRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSocialLogin;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserUploadAvatarRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.JwtResponse;
import com.ptitB22CN539.QuizRemake.Entity.JwtEntity;
import com.ptitB22CN539.QuizRemake.Entity.UserEntity;
import com.ptitB22CN539.QuizRemake.Entity.UserEntity_;
import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Common.Jwt.JwtGenerator;
import com.ptitB22CN539.QuizRemake.Mapper.UserMapper;
import com.ptitB22CN539.QuizRemake.Repository.IJwtRepository;
import com.ptitB22CN539.QuizRemake.Repository.IUserRepository;
import com.ptitB22CN539.QuizRemake.Utils.FileGoogleDrive;
import com.ptitB22CN539.QuizRemake.Utils.PaginationUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.ArrayList;
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

    @Value(value = "${google.clientId}")
    private String clientId;
    @Value(value = "${google.clientSecret}")
    private String clientSecret;
    @Value(value = "${maxLoginDevice}")
    private Long maxLoginDevice;

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
    public JwtResponse login(UserLoginRequest userLogin) {
        UserEntity user = this.getUserByEmail(userLogin.getEmail());
        if ((userLogin.getIsSocial() == null || !userLogin.getIsSocial())
                && !passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            throw new DataInvalidException(ExceptionVariable.EMAIL_PASSWORD_NOT_CORRECT);
        }
        user.getListJwts().removeIf(jwtEntity -> jwtEntity.getExpires().before(new Date(System.currentTimeMillis())));
        if (user.getListJwts().size() >= maxLoginDevice) {
            throw new DataInvalidException(ExceptionVariable.ACCOUNT_LOGIN_MAX_DEVICE);
        }
        if (user.getStatus().equals(UserStatus.INACTIVE)) {
            throw new DataInvalidException(ExceptionVariable.USER_LOCKED);
        }
        JwtDTO jwt = jwtGenerator.generateJwtEntity(user);
        user.getListJwts().add(new JwtEntity(jwt.getId(), jwt.getExpires(), user));
        userRepository.save(user);
        return JwtResponse.builder()
                .expires(jwt.getExpires())
                .token(jwt.getToken())
                .id(jwt.getId())
                .build();
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
    @Transactional(readOnly = true)
    public Page<UserEntity> getAllUsers(UserSearchRequest request) {
        Specification<UserEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get(UserEntity_.EMAIL),
                        String.join("", "%", request.getEmail(), "%")));
            }
            if (StringUtils.hasText(request.getFullName())) {
                predicates.add(criteriaBuilder.like(root.get(UserEntity_.FULL_NAME),
                        String.join("", "%", request.getFullName(), "%")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PaginationUtils.getPageable(request.getPage(), request.getLimit(), Sort.by(Sort.Direction.ASC, UserEntity_.FULL_NAME));
        return userRepository.findAll(specification, pageable);
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
    public JwtResponse loginSocial(UserSocialLogin userSocialLogin) {
        WebClient webClient = WebClient.create();
        MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add(OAuth2ParameterNames.CLIENT_ID, clientId);
        values.add(OAuth2ParameterNames.CLIENT_SECRET, clientSecret);
        values.add(OAuth2ParameterNames.CODE, userSocialLogin.getCode());
        values.add(OAuth2ParameterNames.REDIRECT_URI, "http://localhost:3000/login");
        values.add(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
        WebClient.RequestHeadersSpec<?> headersSpec = webClient.method(HttpMethod.POST)
                .uri("https://www.googleapis.com/oauth2/v4/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromMultipartData(values));
        Mono<Map> responseAccessToken = headersSpec.exchangeToMono(res -> {
            if (res.statusCode().is2xxSuccessful()) {
                return res.bodyToMono(Map.class);
            } else if (res.statusCode().is4xxClientError()) {
                throw new DataInvalidException(ExceptionVariable.CODE_INVALID);
            } else {
                throw new DataInvalidException(ExceptionVariable.SERVER_ERROR);
            }
        });
        String accessToken = Objects.requireNonNull(responseAccessToken.block()).get(OAuth2ParameterNames.ACCESS_TOKEN).toString();
        Mono<Map> responseUserInfo = webClient.method(HttpMethod.GET)
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .exchangeToMono(res -> {
                    if (res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(Map.class);
                    } else {
                        throw new DataInvalidException(ExceptionVariable.SERVER_ERROR);
                    }
                });
        Map userInfo = responseUserInfo.block();
        String email = Objects.requireNonNull(userInfo).get("email").toString();
        if (userRepository.existsByEmail(email)) {
            return this.login(new UserLoginRequest(email, null, true));
        }
        String fullName = Objects.requireNonNull(userInfo).get("name").toString();
        String avatar = Objects.requireNonNull(userInfo).get("picture").toString();
        UserEntity user = userMapper.registerToEntity(new UserRegisterRequest(fullName, email, ConstantConfig.DEFAULT_PASSWORD, ConstantConfig.DEFAULT_PASSWORD, null, UserStatus.ACTIVE));
        user.setAvatar(avatar);
        JwtDTO jwt = jwtGenerator.generateJwtEntity(user);
        JwtEntity jwtEntity = new JwtEntity(jwt.getId(), jwt.getExpires(), user);
        user.setListJwts(List.of(jwtEntity));
        userRepository.save(user);
        return JwtResponse.builder()
                .expires(jwt.getExpires())
                .token(jwt.getToken())
                .id(jwt.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countALlUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional
    public UserEntity uploadAvatar(UserUploadAvatarRequest avatar) {
        String email = avatar.getEmail() == null ? SecurityContextHolder.getContext().getAuthentication().getName() : avatar.getEmail();
        UserEntity user = this.getUserByEmail(email);
        // delete file if exists and not login social
        if (user.getAvatar() != null && !user.getAvatar().contains("https")) {
            FileGoogleDrive.deleteAvatar(user.getAvatar());
        }
        String id = FileGoogleDrive.uploadFileGoogleDrive(avatar.getAvatar());
        user.setAvatar(id);
        return userRepository.save(user);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void test() {
        this.jwtRepository.deleteByExpiresBefore(new Date(System.currentTimeMillis()));
    }
}
