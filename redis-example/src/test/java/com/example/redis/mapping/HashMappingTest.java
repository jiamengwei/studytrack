package com.example.redis.mapping;

import com.example.redis.ExampleApplication;
import com.example.redis.entity.Person;
import com.example.redis.mapping.HashMapping;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ExampleApplication.class)
@RunWith(SpringRunner.class)
public class HashMappingTest {

    @Autowired
    private HashMapping hashMapping;

    @Test
    public void writeHash() {
        Person person = new Person();
        person.setFirstname("jia").setLastname("mengwei");
        hashMapping.writeHash("user",person);

        Person person2 = hashMapping.loadHash("user");
        Assert.assertEquals(person, person2);
    }
}