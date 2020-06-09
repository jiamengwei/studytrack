package com.elastic.demo;

import com.elastic.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonDemo {
    @SneakyThrows
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User(1, "Paul");
        String userJson = objectMapper.writeValueAsString(user);
        System.out.println(userJson);
    }

}
