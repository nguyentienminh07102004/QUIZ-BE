package com.ptitB22CN539.QuizRemake.Service.Role;

import com.ptitB22CN539.QuizRemake.Entity.RoleEntity;
import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.Repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public RoleEntity findByCode(String code) {
        return roleRepository.findById(code)
                .orElseThrow(() -> new DataInvalidException(ExceptionVariable.ROLE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return roleRepository.existsById(code);
    }

    @Override
    @Transactional
    public RoleEntity save(RoleEntity role) {
        return roleRepository.save(role);
    }
}
