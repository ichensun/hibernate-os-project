package com.bfs.hibernateprojectdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {
    private String name;
    private String description;
    private Double wholesalePrice;
    private Double retailPrice;
    private Integer quantity;
}