package com.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "user", createIndex = false)
public class User implements Serializable {

    private Integer id;
    private String username;
    private String address;
    private LocalDateTime birthday;
}
