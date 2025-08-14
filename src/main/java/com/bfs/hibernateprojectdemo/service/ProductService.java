package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.ProductDao;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.dto.CreateProductRequest;
import com.bfs.hibernateprojectdemo.dto.ProductDetailDTO;
import com.bfs.hibernateprojectdemo.dto.StatsDTO;
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

    public List<StatsDTO> getTopRecentPurchasedProducts(Long userId, int limit) {
        return productDao.findTopPurchasedProductsByUser(userId, limit);
    }

    public Long addNewProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setWholesalePrice(request.getWholesalePrice());
        product.setRetailPrice(request.getRetailPrice());
        product.setQuantity(request.getQuantity());

        return productDao.saveProduct(product);
    }

    public void updateProduct(Long productId, UpdateProductRequest request) {
        productDao.updateProduct(productId, request);
    }
}
