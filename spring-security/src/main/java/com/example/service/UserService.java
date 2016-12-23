package com.example.service;

import com.example.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public void create(User user);

    public Iterable<User> getAllUsers();

    public void delete(Long id);
}
