package com.ptitB22CN539.QuizRemake.Service.Role;

import com.ptitB22CN539.QuizRemake.Domains.RoleEntity;

public interface IRoleService {
    RoleEntity findByCode(String code);
    boolean existsByCode(String code);
    RoleEntity save(RoleEntity role);
}
