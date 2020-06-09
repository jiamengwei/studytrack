package com.example.redis.mapping;

import com.example.redis.ExampleApplication;
import com.example.redis.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.*;

@SpringBootTest(classes = ExampleApplication.class)
@RunWith(SpringRunner.class)
public class Jackson2HashMappingTest {
    @Autowired
    private Jackson2HashMapping mapping;

    @Test
    public void writeHash() {
        String key = "jackson:demo:user";
        User user = new User();
        user.setFirstname("li");
        user.setLastname("aee");
        user.setDate(new Date());
        user.setLocalDateTime(LocalDateTime.now());
        user.setAddress(new User.Address("NanJing","China"));

        mapping.writeHash(key, user);
        User user2 = mapping.loadHash(key);
        System.out.println(user2);
        Assert.assertEquals(user, user2);

    }
}