package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.dto.CreateProductRequest;
import com.bfs.hibernateprojectdemo.dto.ProductDetailDTO;
import com.bfs.hibernateprojectdemo.dto.UpdateProductRequest;
import com.bfs.hibernateprojectdemo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // user & admin
    @GetMapping("/products/all")
    public List<Product> getAllInStockProducts() {
        return productService.getAllInStockProducts();
    }

    // user & admin
    @GetMapping("/products/{id}")
    public ProductDetailDTO getProductDetailById(@PathVariable Long id) {
        return productService.getProductDetailById(id);
    }

    // listing
    // admin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<String> addProduct(@RequestBody CreateProductRequest request) {
        productService.addNewProduct(request);
        return ResponseEntity.ok("Product added successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/products/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @RequestBody UpdateProductRequest request) {
        productService.updateProduct(productId, request);
        return ResponseEntity.ok("Product updated successfully");
    }
}
