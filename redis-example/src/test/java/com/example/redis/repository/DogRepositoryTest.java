package com.example.redis.repository;

import com.example.redis.ExampleApplication;
import com.example.redis.entity.Dog;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@Slf4j
@SpringBootTest(classes = ExampleApplication.class)
@RunWith(SpringRunner.class)
public class DogRepositoryTest {

    @Autowired
    private DogRepository dogRepository;

    @Test
    public void dogTest(){
        Dog dog = new Dog("1","J","B");
        dogRepository.save(dog);

        Optional<Dog> dogOpt = dogRepository.findById("1");
        dogOpt.orElseThrow(()->new IllegalStateException("dog not exist"));

        long count = dogRepository.count();
        Assert.assertEquals(1, count);
        
    }

}