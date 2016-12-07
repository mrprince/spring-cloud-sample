package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataRestApplicationTests {
    @Autowired
    UserRepository userRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("chester" + i);
            userRepository.save(user);
        }

        System.out.println("---------->>>" + userRepository.findByName("chester0"));
        System.out.println("---------->>>" + userRepository.findByName("chester0"));

        userRepository.findOne(1);

        //userRepository.delete(userRepository.findOne(1));

        //System.out.println("---------->>>" + userRepository.findByName("chester0"));

    }

}
