package com.example.service.impl;

import com.example.entity.User;
import com.example.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUserById() throws Exception {
        User user = userService.getUserById(1L);
        assertThat(user.getUserName()).isEqualTo("admin");
    }

    @Test
    public void getAllUsers() {
        Pageable pageable = new PageRequest(0, 1, Sort.Direction.ASC, "id");
        Page<User> users = userService.getAllUsers(pageable);
        User user = users.getContent().get(0);
        assertThat(users).hasSize(1);
        assertThat(user.getUserName()).isEqualTo("admin");
    }

    @Test
    public void CURD(){
        //Add
        User user = new User();
        user.setUserName("test");
        user.setPassword("test");
        user.setEmail("test@test.com");
        user = userService.save(user);
        assertThat(user.getId()).isNotZero();
        //Update
        user.setUserName("testok");
        user = userService.save(user);
        assertThat(user.getUserName()).isEqualTo("testok");
        //Delete
        userService.delete(user.getId());
        user = userService.getUserById(user.getId());
        assertThat(user).isNull();
    }

}