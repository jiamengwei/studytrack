package com.example.redis.mapping;

import com.example.redis.entity.Person;
import com.example.redis.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Jackson2HashMapping {

  @Autowired
  HashOperations<Object, String, Object> jacksonHashOperations;

  HashMapper<Object, String, Object> mapper = new Jackson2HashMapper(false);

  public void writeHash(String key, User user) {
    Map<String, Object> mappedHash = mapper.toHash(user);
    jacksonHashOperations.putAll(key, mappedHash);
  }

  public User loadHash(String key) {
    Map<String, Object> loadedHash = jacksonHashOperations.entries(key);
    Object object = mapper.fromHash(loadedHash);
    return (User) object;
  }
}