package com.example.service.impl;

import com.example.dao.RoleRepository;
import com.example.entity.Role;
import com.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Iterable<Role> getRoles() {
        return roleRepository.findAll();
    }
}
