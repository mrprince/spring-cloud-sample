package com.example;

import com.example.dao.UserRepository;
import com.example.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataRestApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
    }


    @Test
    public void test() {

        Arrays.asList("tom,jerry,admin".split(",")).forEach(name -> userRepository.save(new User(null, name, new Date())));
        System.out.println("----------->>>" + userRepository.findAll());

        Integer pageNumber = 1;
        Integer pageSize = 5;
        PageRequest page = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "createDate");
        userRepository.findAll(page).forEach(user -> System.out.println("----->>" + user));

        userRepository.findAll(page).forEach(user -> System.out.println("----->>" + user));

        //System.out.println("@@@@@@@@@"+userRepository.findAll(page));

        System.out.println("-->" + userRepository.findOne(1));


    }

}
