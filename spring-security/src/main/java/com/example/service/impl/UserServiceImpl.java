package com.example.service.impl;

import com.example.dao.UserRepository;
import com.example.entity.User;
import com.example.service.UserService;
import org.audit4j.core.annotation.Audit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Audit(action = "save user")
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Audit(action = "delete user")
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username);
    }

}
