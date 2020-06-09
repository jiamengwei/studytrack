package com.example.redis.repository;

import com.example.redis.entity.Dog;
import org.springframework.data.repository.CrudRepository;

public interface DogRepository extends CrudRepository<Dog, String> {

}