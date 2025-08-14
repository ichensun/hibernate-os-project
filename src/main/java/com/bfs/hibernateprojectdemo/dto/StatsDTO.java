package com.bfs.hibernateprojectdemo.dto;

import com.bfs.hibernateprojectdemo.domain.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsDTO {

    private Product product;
    private Long totalQuantity;
}
