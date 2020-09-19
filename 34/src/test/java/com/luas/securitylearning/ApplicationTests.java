package com.luas.securitylearning;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));

        System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
    }

}
