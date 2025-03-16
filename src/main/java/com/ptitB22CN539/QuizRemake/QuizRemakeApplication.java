package com.ptitB22CN539.QuizRemake;

import com.ptitB22CN539.QuizRemake.BeanApp.ConstantConfig;
import com.ptitB22CN539.QuizRemake.Domains.RoleEntity;
import com.ptitB22CN539.QuizRemake.Service.Role.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class QuizRemakeApplication {
    private final IRoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(QuizRemakeApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            if (!roleService.existsByCode(ConstantConfig.ROLE_ADMIN)) {
                roleService.save(new RoleEntity("Quản trị viên", ConstantConfig.ROLE_ADMIN));
            }
            if (!roleService.existsByCode(ConstantConfig.ROLE_USER)) {
                roleService.save(new RoleEntity("Nguời dùng", ConstantConfig.ROLE_USER));
            }
        };
    }
}
