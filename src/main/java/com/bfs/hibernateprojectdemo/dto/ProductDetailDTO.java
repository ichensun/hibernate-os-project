package com.bfs.hibernateprojectdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailDTO {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("name")
    private String name;

    @JsonProperty("retail_price")
    private Double retailPrice;

    @JsonProperty("wholesale_price")
    private Double wholesalePrice;
}
