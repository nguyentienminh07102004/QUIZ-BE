package com.ptitB22CN539.QuizRemake.Service.User;

import com.nimbusds.jwt.JWTClaimsSet;
import com.ptitB22CN539.QuizRemake.Common.Bean.ConstantConfiguration;
import com.ptitB22CN539.QuizRemake.Common.Enum.UserStatus;
import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Common.Jwt.JwtGenerator;
import com.ptitB22CN539.QuizRemake.DTO.DTO.JwtDTO;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserChangeAdmin;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserChangePasswordRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserForgotChangePassword;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserForgotPassword;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserLoginRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserRegisterRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSearchRequest;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserSocialLogin;
import com.ptitB22CN539.QuizRemake.DTO.Request.User.UserUploadAvatarRequest;
import com.ptitB22CN539.QuizRemake.DTO.Response.JwtResponse;
import com.ptitB22CN539.QuizRemake.Mapper.UserMapper;
import com.ptitB22CN539.QuizRemake.Model.Entity.JwtEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.UserEntity;
import com.ptitB22CN539.QuizRemake.Model.Entity.UserEntity_;
import com.ptitB22CN539.QuizRemake.JpaRepository.IJwtRepository;
import com.ptitB22CN539.QuizRemake.JpaRepository.IUserRepository;
import com.ptitB22CN539.QuizRemake.Service.Role.IRoleService;
import com.ptitB22CN539.QuizRemake.Utils.EmailUtils;
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
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserMapper userMapper;
    private final IUserRepository userRepository;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;
    private final IJwtRepository jwtRepository;
    private final EmailUtils emailUtils;
    private final HashOperations<String, String, Object> hashOperations;
    private final RedisTemplate<String, Object> redisTemplate;
    private final IRoleService roleService;

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
        UserEntity user = userMapper.registerToEntity(new UserRegisterRequest(fullName, email, ConstantConfiguration.DEFAULT_PASSWORD, ConstantConfiguration.DEFAULT_PASSWORD, null, UserStatus.ACTIVE));
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
            FileGoogleDrive.deleteFileGoogleImage(user.getAvatar());
        }
        String id = FileGoogleDrive.uploadFileGoogleDrive(avatar.getAvatar());
        user.setAvatar(id);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void forgotPassword(UserForgotPassword userForgotPassword) {
        String code = UUID.randomUUID().toString();
        this.hashOperations.put("codeForgot:%s".formatted(code), "email", userForgotPassword.getEmail());
        this.redisTemplate.expire("%s:%s".formatted(userForgotPassword.getEmail(), code), Duration.ofSeconds(300));
        this.emailUtils.sendEmail(userForgotPassword.getEmail(), "ForgotPassword", "ForgotPassword", Map.of("code", code));
    }

    @Override
    @Transactional
    public void forgotPassword(UserForgotChangePassword userForgotChangePassword) {
        String code = userForgotChangePassword.getCode();
        Object email = this.hashOperations.get("codeForgot:%s".formatted(code), "email");
        if (email == null) {
            throw new DataInvalidException(ExceptionVariable.CODE_INVALID);
        }
        if (!userForgotChangePassword.getConfirmPassword().equals(userForgotChangePassword.getNewPassword())) {
            throw new DataInvalidException(ExceptionVariable.PASSWORD_CONFIRM_PASSWORD_NOT_MATCH);
        }
        UserEntity user = this.getUserByEmail(email.toString());
        user.setPassword(passwordEncoder.encode(userForgotChangePassword.getNewPassword()));
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeAdmin() {
        UserEntity user = this.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!user.getRole().getCode().equals(ConstantConfiguration.ROLE_ADMIN)) {
            throw new DataInvalidException(ExceptionVariable.FORBIDDEN);
        }
        String code = UUID.randomUUID().toString();
        this.hashOperations.put("changeAdmin:%s".formatted(user.getEmail()), "code", code);
        this.redisTemplate.expire("changeAdmin:%s".formatted(user.getEmail()), Duration.ofSeconds(300));
        this.emailUtils.sendEmail(user.getEmail(), "You want to change admin", "ChangeAdmin", Map.of("code", code));
    }

    @Override
    @Transactional
    public void changeAdmin(UserChangeAdmin userChangeAdmin) {
        UserEntity user = this.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Object code = this.hashOperations.get("changeAdmin:%s".formatted(user.getEmail()), "code");
        if (code == null) {
            throw new DataInvalidException(ExceptionVariable.CODE_INVALID);
        }
        code = code.toString();
        if (!userChangeAdmin.getCode().equals(code)) {
            throw new DataInvalidException(ExceptionVariable.CODE_INVALID);
        }
        if (userChangeAdmin.getEmail().equals(user.getEmail())) {
            throw new DataInvalidException(ExceptionVariable.EMAIL_ADMIN_OLD_AND_NEW_MATCH);
        }
        code = UUID.randomUUID().toString();
        this.redisTemplate.opsForValue().set("VerifyChangeAdminCode", code);
        this.redisTemplate.opsForValue().set("VerifyChangeAdminEmail", userChangeAdmin.getEmail());
        this.redisTemplate.expire("VerifyChangeAdminCode", Duration.ofSeconds(300));
        this.emailUtils.sendEmail(userChangeAdmin.getEmail(), "Change admin for you", "VerifyChangeAdmin", Map.of("confirmationLink", "http://localhost:8080/api/v2/users/change-admin?code=%s".formatted(code)));
    }

    @Override
    @Transactional
    public void verifyChangeAdmin(String code) {
        Object codeVerify = this.redisTemplate.opsForValue().get("VerifyChangeAdminCode");
        if (codeVerify == null) {
            throw new DataInvalidException(ExceptionVariable.CODE_INVALID);
        }
        if (!code.equals(codeVerify.toString())) {
            throw new DataInvalidException(ExceptionVariable.CODE_INVALID);
        }
        UserEntity user = this.userRepository.findByRole_Code(ConstantConfiguration.ROLE_ADMIN);
        String email = String.valueOf(this.redisTemplate.opsForValue().get("VerifyChangeAdminEmail"));
        if (this.userRepository.existsByEmail(email)) {
            UserEntity userEntity = this.getUserByEmail(email);
            userEntity.setRole(this.roleService.findByCode(ConstantConfiguration.ROLE_ADMIN));
            this.userRepository.save(userEntity);
            this.userRepository.delete(user);
        } else {
            user.setEmail(email);
            this.userRepository.save(user);
        }
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteJwtExpire() {
        this.jwtRepository.deleteByExpiresBefore(new Date(System.currentTimeMillis()));
    }
}
