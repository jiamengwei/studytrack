package com.shardingsphere.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.shardingsphere.example.mapper")
@SpringBootApplication
public class ShardingsphereExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingsphereExampleApplication.class, args);
    }
}
