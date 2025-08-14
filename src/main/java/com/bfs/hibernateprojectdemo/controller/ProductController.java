package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.dto.CreateProductRequest;
import com.bfs.hibernateprojectdemo.dto.ProductDetailDTO;
import com.bfs.hibernateprojectdemo.dto.UpdateProductRequest;
import com.bfs.hibernateprojectdemo.dto.common.DataResponse;
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

import javax.xml.crypto.Data;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // user & admin
    @GetMapping("/products/all")
    public ResponseEntity<DataResponse> getAllInStockProducts() {
        List<Product> products = productService.getAllInStockProducts();

        DataResponse dataResponse = DataResponse.builder()
                .code(200)
                .data(products)
                .message("Get all in-stock products")
                .build();

        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<DataResponse> getProductDetailById(@PathVariable Long id) {
        ProductDetailDTO productDetailDTO = productService.getProductDetailById(id);

        return ResponseEntity.ok(DataResponse.builder()
                        .code(200).data(productDetailDTO).message("Get " +
                        "product detail by id").build());
    }

    // listing
    // admin add product
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<DataResponse> addProduct(@RequestBody CreateProductRequest request) {
        Long productId = productService.addNewProduct(request);

        return ResponseEntity.ok(
                DataResponse.builder()
                        .code(200)
                        .data(productId)
                        .message("Product added")
                        .build());
    }

    // admin update product
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/products/{productId}")
    public ResponseEntity<DataResponse> updateProduct(@PathVariable Long productId,
                                                      @RequestBody UpdateProductRequest request) {
        productService.updateProduct(productId, request);

        return ResponseEntity.ok(
                DataResponse.builder()
                        .code(200)
                        .data(null)
                        .message("Product updated")
                        .build());
    }
}
