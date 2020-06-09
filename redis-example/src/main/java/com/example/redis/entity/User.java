package com.example.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 7329196549767900747L;

    String firstname;
    String lastname;
    Address address;
    Date date;
    LocalDateTime localDateTime;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Address implements Serializable {
        String city;
        String country;
    }
}
