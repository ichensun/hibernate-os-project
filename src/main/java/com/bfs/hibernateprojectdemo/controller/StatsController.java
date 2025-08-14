package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.dto.StatsDTO;
import com.bfs.hibernateprojectdemo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole(0)")
    @GetMapping("/products/recent/{limit}/user/{userId}")
    public List<StatsDTO> getRecentlyPurchasedProducts(@PathVariable Long userId,
                                                       @PathVariable Integer limit) {
        return productService.getTopPurchasedProducts(userId, limit);
    }
}
