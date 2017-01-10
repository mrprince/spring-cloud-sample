package com.example.service;

import com.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    //save user
    User save(User user);

    //delete user
    void delete(Long id);

    //query user
    Page<User> getAllUsers(Pageable pageable);

    //find user by id
    User getUserById(Long id);
}
