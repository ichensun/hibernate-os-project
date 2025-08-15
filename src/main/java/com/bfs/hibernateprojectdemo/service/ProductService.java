package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.ProductDao;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.dto.CreateProductRequest;
import com.bfs.hibernateprojectdemo.dto.ProductDetailDTO;
import com.bfs.hibernateprojectdemo.dto.ProductDTO;
import com.bfs.hibernateprojectdemo.dto.UpdateProductRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

@Service
public class ProductService {

    private ProductDao productDao;

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllInStockProducts() {
        return productDao.getAllInStockProducts();
    }

    @Transactional
    public ProductDetailDTO getProductDetailById(Long id) {
        Product product = productDao.getProductDetailById(id);

        return ProductDetailDTO.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .retailPrice(product.getRetailPrice())
                .wholesalePrice(product.getWholesalePrice())
                .build();
    }

    public List<ProductDTO> getMostRecentPurchasedProducts(Long userId, int limit) {
        return productDao.getMostRecentPurchasedProducts(userId, limit);
    }

    public Long addNewProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .wholesalePrice(request.getWholesalePrice())
                .retailPrice(request.getRetailPrice())
                .quantity(request.getQuantity())
                .build();

        return productDao.saveProduct(product);
    }

    public void updateProduct(Long productId, UpdateProductRequest request) {
        productDao.updateProduct(productId, request);
    }
}
