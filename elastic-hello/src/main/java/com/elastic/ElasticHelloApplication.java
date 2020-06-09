package com.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
@SpringBootApplication
public class ElasticHelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticHelloApplication.class, args);
    }
}
