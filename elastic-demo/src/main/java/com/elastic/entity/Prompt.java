package com.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NegativeOrZero;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prompt {
    private List<Project> items;
}
