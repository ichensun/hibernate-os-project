package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.dto.ProductDetailDTO;
import com.bfs.hibernateprojectdemo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/all")
    public List<Product> getAllInStockProducts() {
        return productService.getAllInStockProducts();
    }

    @GetMapping("/products/{id}")
    public ProductDetailDTO getProductDetailById(@PathVariable Long id) {
        return productService.getProductDetailById(id);
    }
}
