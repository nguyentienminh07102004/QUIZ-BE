package com.ptitB22CN539.QuizRemake;

import com.ptitB22CN539.QuizRemake.Common.Bean.ConstantConfiguration;
import com.ptitB22CN539.QuizRemake.Model.Entity.RoleEntity;
import com.ptitB22CN539.QuizRemake.Service.Role.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
@EnableJpaAuditing
@EnableAsync
@EnableJpaRepositories
public class QuizRemakeApplication {
    private final IRoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(QuizRemakeApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            if (!roleService.existsByCode(ConstantConfiguration.ROLE_ADMIN)) {
                roleService.save(new RoleEntity("Quản trị viên", ConstantConfiguration.ROLE_ADMIN));
            }
            if (!roleService.existsByCode(ConstantConfiguration.ROLE_USER)) {
                roleService.save(new RoleEntity("Nguời dùng", ConstantConfiguration.ROLE_USER));
            }
        };
    }
}
