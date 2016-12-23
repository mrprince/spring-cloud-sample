package com.example.service.impl;

import com.example.dao.UserRepository;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public void create(User user) {
        userRepository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username);
    }

}
