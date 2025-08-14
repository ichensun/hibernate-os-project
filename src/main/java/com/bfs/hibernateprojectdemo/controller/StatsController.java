package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.dto.StatsDTO;
import com.bfs.hibernateprojectdemo.dto.common.DataResponse;
import com.bfs.hibernateprojectdemo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

//    @PreAuthorize("hasRole('USER')")
    @GetMapping("/products/recent/{limit}/user/{userId}")
    public ResponseEntity<DataResponse> getRecentlyPurchasedProducts(@PathVariable Long userId,
                                                                     @PathVariable Integer limit) {
        List<StatsDTO> stats = productService.getTopRecentPurchasedProducts(userId, limit);

        DataResponse dataResponse = DataResponse.builder()
                .data(stats)
                .message("Most recently purchased product")
                .build();

        return ResponseEntity.ok().body(dataResponse);
    }
}
